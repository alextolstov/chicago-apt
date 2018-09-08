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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.chicago.ext.dal.cassandra.CassandraConstants.BRANCHES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.COMPANIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.HOLDINGS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.PERMISSIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.ROLES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_EMAIL_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_ID_TABLE;
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
        Statement query = QueryBuilder.insertInto(KEYSPACE, USERS_BY_ID_TABLE)
                .value("user_id", newUserId)
                .value("email", newUser.getEmail())
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

        Row row;
        while ((row = result.one()) != null)
        {
            Set<Integer> rolePermissionsIds = row.getSet("permission_ids", Integer.class);
            permissionIds.addAll(rolePermissionsIds);
        }
        // Get permissions as strings
        query = QueryBuilder.select()
                .from(KEYSPACE, PERMISSIONS_TABLE)
                .where(QueryBuilder.in("permission_id", permissionIds));
        result = _cassandraConnector.getSession().execute(query);

        Set<String> permissions = new HashSet<>();

        while ((row = result.one()) != null)
        {
            permissions.add(row.getString("permission"));
        }

        // Cassandra update same as insert if condition not true
        Set<Integer> roleIdsSet = new HashSet<>(userPermissions.getRolesList());
        Set<Integer> permissionIdsSet = new HashSet<>(userPermissions.getExtraPermissionsList());
        query = QueryBuilder.update(KEYSPACE, USER_PERMISSIONS_TABLE)
                .with(QueryBuilder.set("role_ids", roleIdsSet))
                .and(QueryBuilder.set("extra_permission_ids", permissionIdsSet))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userPermissions.getUserId())));
        _cassandraConnector.getSession().execute(query);

        query = QueryBuilder.select().from(KEYSPACE, USERS_BY_ID_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userPermissions.getUserId())));
        result = _cassandraConnector.getSession().execute(query);
        Row userRow = result.one();
        String email = userRow.getString("email");
        query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE).with(QueryBuilder.set("permissions", permissions))
                .where(QueryBuilder.eq("email", email));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void setUserAvatar(UserOuterClass.UserAvatar avatar)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
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
        Row row = result.one();
        if (row == null)
        {
            throw new UserPermissionsNotFoundException("No permissions found for user with user Id " + userId);
        }

        return UserOuterClass.UserPermissions.newBuilder()
                .setUserId(row.getUUID("user_id").toString())
                .addAllRoles(row.getSet("role_ids", Integer.class))
                .addAllExtraPermissions(row.getSet("extra_permission_ids", Integer.class))
                .build();
    }

    @Override
    public UserOuterClass.User getUser(String email) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_BY_EMAIL_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }

        // Populate protobuf
        return buildUser(row);
    }

    @Override
    public Pair<String, byte[]> getHashSalt(String email) throws Exception
    {
        Statement query = QueryBuilder.select("password_hash", "password_salt")
                .from(KEYSPACE, USERS_BY_EMAIL_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();

        if (row == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }
        return new Pair<>(row.getString("password_hash"), row.getBytes("password_salt").array());
    }

    @Override

    // We dont care what organization type, just dig inside 3 levels. Cassandra give use very cheap read
    public List<UserOuterClass.User> getUsers(String organizationId)
    {
        List<UUID> usersId = getOrganizationUsers(organizationId, HOLDINGS_TABLE);
        if (usersId.size() == 0)
        {
            usersId = getOrganizationUsers(organizationId, COMPANIES_TABLE);
        }
        if (usersId.size() == 0)
        {
            usersId = getOrganizationUsers(organizationId, BRANCHES_TABLE);
        }

        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_BY_ID_TABLE)
                .where(QueryBuilder.in("user_id", usersId));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row;
        List<UserOuterClass.User> users = new ArrayList<>();

        while((row = result.one()) != null)
        {
            users.add(buildUser(row));
        }

        return users;
    }

    private List<UUID> getOrganizationUsers(String tableName, String organizationId)
    {
        List<UUID> users = new ArrayList<>();
        Statement query = QueryBuilder.select("users")
                .from(KEYSPACE, tableName)
                .where(QueryBuilder.eq("holding_id", UUID.fromString(organizationId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();

        if (row != null)
        {
            Set<UUID> usersSet = row.getSet("users", UUID.class);
            if (usersSet != null)
            {
                users = new ArrayList<>(usersSet);
            }
        }
        LOG.info("Found {} users of organizationId {} in table {}", users.size(), organizationId, tableName);
        return users;
    }

    private UserOuterClass.User buildUser(Row row)
    {
        UserOuterClass.User.Builder builder = UserOuterClass.User.newBuilder();
        // Always non empty
        builder.setUserId(row.getUUID("user_id").toString());
        builder.setEmail(row.getString("email"));
        builder.setCreateDatetime(row.getTimestamp("create_datetime").getTime());
        // Could be empty
        if (row.getBytes("avatar") != null) builder.setAvatar(ByteString.copyFrom(row.getBytes("avatar").array()));
        if (row.getString("first_name") != null) builder.setFirstName(row.getString("first_name"));
        if (row.getString("middle_name") != null) builder.setMiddleName(row.getString("middle_name"));
        if (row.getString("last_name") != null) builder.setLastName(row.getString("last_name"));
        if (row.getString("cell_phone") != null) builder.setCellPhone(row.getString("cell_phone"));
        if (row.getString("home_phone") != null) builder.setHomePhone(row.getString("home_phone"));
        if (row.getString("work_phone") != null) builder.setWorkPhone(row.getString("work_phone"));
        if (row.getUUID("holding_id") != null) builder.setHoldingId(row.getUUID("holding_id").toString());
        if (row.getUUID("company_id") != null) builder.setCompanyId(row.getUUID("company_id").toString());
        if (row.getUUID("branch_id") != null) builder.setBranchId(row.getUUID("branch_id").toString());
        if (row.getUUID("address_id") != null) builder.setAddressId(row.getUUID("address_id").toString());

        return builder.build();
    }

    @Override
    public boolean isUserExists(String email)
    {
        Statement query = QueryBuilder.select()
                .countAll()
                .from(KEYSPACE, USERS_BY_EMAIL_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        return (result.one().getLong("count") > 0);
    }

    @Override
    public void updateUser(UserOuterClass.User user)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("first_name", user.getFirstName()))
                .and(QueryBuilder.set("middle_name", user.getMiddleName()))
                .and(QueryBuilder.set("last_name", user.getLastName()))
                .where(QueryBuilder.eq("user_id", UUID.fromString(user.getUserId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void setUserPassword(String user_id, String passwordHash, byte[] passwordSalt)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("password_hash", passwordHash))
                .and(QueryBuilder.set("password_salt", passwordSalt))
                .where(QueryBuilder.eq("user_id", UUID.fromString(user_id)));
        _cassandraConnector.getSession().execute(query);
    }
}
