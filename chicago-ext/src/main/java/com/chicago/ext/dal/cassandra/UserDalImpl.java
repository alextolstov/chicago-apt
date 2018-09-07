package com.chicago.ext.dal.cassandra;

import com.chicago.common.util.TimeUtil;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.UserDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import com.google.protobuf.ByteString;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.PERMISSIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.ROLES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USER_PERMISSIONS_TABLE;

public class UserDalImpl implements UserDal
{
    private static final Logger LOG = LoggerFactory.getLogger(UserDalImpl.class);

    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public String createUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt)
    {
        // Create and associate user with new company
        UUID newUserId = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, USERS_TABLE)
                .value("email", newUser.getEmail())
                .value("user_id", newUserId)
                .value("password_hash", passwordHash)
                .value("password_salt", ByteBuffer.wrap(passwordSalt))//Bytes.toHexString(passwordSalt))
                .value("first_name", newUser.getFirstName())
                .value("middle_name", newUser.getMiddleName())
                .value("last_name", newUser.getLastName())
                .value("cell_phone", newUser.getCellPhone())
                .value("home_phone", newUser.getHomePhone())
                .value("work_phone", newUser.getWorkPhone())
                .value("company_id", UUID.fromString(newUser.getCompanyId()))
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _cassandraConnector.getSession().execute(query);
        LOG.info("New user {} created", newUser.getEmail());
        return newUserId.toString();
    }

    @Override
    public void setUserPermissions(UserOuterClass.UserPermissions userPermissions)
    {
        Set<Integer> permissionIds = new HashSet<>(userPermissions.getExtraPermissionsList());

        // Expand roles
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ROLES_TABLE)
                .where(QueryBuilder.in("role_id", userPermissions.getRolesList()));
        ResultSet result = _cassandraConnector.getSession().execute(query);

        Row roleRow;
        while ((roleRow = result.one()) != null)
        {
            Set<Integer> rolePermissionsIds = roleRow.getSet("permission_ids", Integer.class);
            permissionIds.addAll(rolePermissionsIds);
        }
        // Get permissions as strings
        query = QueryBuilder.select()
                .from(KEYSPACE, PERMISSIONS_TABLE)
                .where(QueryBuilder.in("permission_id", permissionIds));
        result = _cassandraConnector.getSession().execute(query);

        Set<String> permissions = new HashSet<>();

        Row permissionRow;
        while ((permissionRow = result.one()) != null)
        {
            permissions.add(permissionRow.getString("permission"));
        }

        // Cassandra update same as insert if condition not true
        Set<Integer> roleIdsSet = new HashSet<>(userPermissions.getRolesList());
        Set<Integer> permissionIdsSet = new HashSet<>(userPermissions.getExtraPermissionsList());
        query = QueryBuilder.update(KEYSPACE, USER_PERMISSIONS_TABLE)
                .with(QueryBuilder.set("role_ids", roleIdsSet))
                .and(QueryBuilder.set("extra_permission_ids", permissionIdsSet))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userPermissions.getUserId())));
        _cassandraConnector.getSession().execute(query);

        query = QueryBuilder.select().from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userPermissions.getUserId())));
        result = _cassandraConnector.getSession().execute(query);
        Row userRow = result.one();
        String email = userRow.getString("email");
        query = QueryBuilder.update(KEYSPACE, USERS_TABLE).with(QueryBuilder.set("permissions", permissions))
                .where(QueryBuilder.eq("email", email));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void setUserAvatar(UserOuterClass.UserAvatar avatar)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_TABLE)
                .with(QueryBuilder.set("avatar", avatar.getAvatar()))
                .where(QueryBuilder.eq("user_id", avatar.getUserId()));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception
    {
        // Get all premissions
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USER_PERMISSIONS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row permissionsRow = result.one();
        if (permissionsRow == null)
        {
            throw new UserPermissionsNotFoundException("No permissions found for user with user Id " + userId);
        }

        return UserOuterClass.UserPermissions.newBuilder()
                .setUserId(permissionsRow.getUUID("user_id").toString())
                .addAllRoles(permissionsRow.getSet("role_ids", Integer.class))
                .addAllExtraPermissions(permissionsRow.getSet("extra_permission_ids", Integer.class))
                .build();
    }

    @Override
    public UserOuterClass.User getUser(String email) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row userRow = result.one();
        if (userRow == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }

        // Populate protobuf
        return UserOuterClass.User.newBuilder()
                .setEmail(email)
                .setUserId(userRow.getUUID("user_id").toString())
                .setAvatar(ByteString.copyFrom(userRow.getBytes("avatar").array()))
                .setFirstName(userRow.getString("first_name"))
                .setMiddleName(userRow.getString("middle_name"))
                .setLastName(userRow.getString("last_name"))
                .setCellPhone(userRow.getString("cell_phone"))
                .setHomePhone(userRow.getString("home_phone"))
                .setWorkPhone(userRow.getString("work_phone"))
                .setHoldingId(userRow.getUUID("holding_id").toString())
                .setCompanyId(userRow.getUUID("company_id").toString())
                .setBranchId(userRow.getUUID("branch_id").toString())
                .setAddressId(userRow.getUUID("address_id").toString())
                .addAllPermissions(userRow.getSet("permissions", String.class))
                .setCreateDatetime(userRow.getTimestamp("create_datetime").getTime())
                .build();
    }

    @Override
    public Pair<String, byte[]> getHashSalt(String email) throws Exception
    {
        Statement query = QueryBuilder.select("password_hash", "password_salt")
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();

        if (row == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }
        return new Pair(row.getString("password_hash"), row.getBytes("password_salt").array());
    }

    @Override
    public List<UserOuterClass.User> getUsers()
    {
        return null;
    }

    @Override
    public boolean isUserExists(String email)
    {
        Statement query = QueryBuilder.select()
                .countAll()
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        return (result.one().getLong("count") > 0);
    }

    @Override
    public void updateUser(UserOuterClass.User user)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_TABLE)
                .with(QueryBuilder.set("first_name", user.getFirstName()))
                .and(QueryBuilder.set("middle_name", user.getMiddleName()))
                .and(QueryBuilder.set("last_name", user.getLastName()))
                .where(QueryBuilder.eq("user_id", UUID.fromString(user.getUserId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void updateUserPassword(String user_id, String passwordHash, byte[] passwordSalt)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_TABLE)
                .with(QueryBuilder.set("password_hash", passwordHash))
                .and(QueryBuilder.set("password_salt", passwordSalt))
                .where(QueryBuilder.eq("user_id", user_id));
        _cassandraConnector.getSession().execute(query);
    }
}
