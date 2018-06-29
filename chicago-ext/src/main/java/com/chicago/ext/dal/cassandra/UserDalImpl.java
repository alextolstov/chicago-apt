package com.chicago.ext.dal.cassandra;

import com.chicago.common.core.AbstractComponent;
import com.chicago.ext.dal.UserDal;
import com.chicago.common.util.PasswordUtil;
import com.chicago.common.util.TimeUtil;
import com.chicago.dto.UserOuterClass;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.COMPANIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.PERMISSIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.ROLES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USER_PERMISSIONS_TABLE;
import static java.util.Arrays.asList;

public class UserDalImpl implements UserDal
{
    private static final Logger _LOG = LoggerFactory.getLogger(AbstractComponent.class);

    private ServiceLocator _locator;

    public UserDalImpl()
    {
        _locator = ServiceLocatorFactory.getInstance().find("servicelocator");
    }

    public String registerUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception
    {
        if (isExists(newUser.getEmail()))
        {
            throw new Exception("User " + newUser.getEmail() + " already exists");
        }

        UUID newComanyId = UUIDs.random();
        // Create company first
        Statement query = QueryBuilder.insertInto(KEYSPACE, COMPANIES_TABLE)
                .value("company_id", newComanyId)
                .value("name", "New company")
                .value("description", "New company")
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _locator.getService(CassandraConnector.class).getSession().execute(query);
        _LOG.info("New company created for user {}", newUser.getEmail());

        // Create and associate user with new company
        UUID newUserId = UUIDs.random();
        query = QueryBuilder.insertInto(KEYSPACE, USERS_TABLE)
                .value("user_id", newUserId)
                .value("email", newUser.getEmail())
                .value("password_hash", passwordHash)
                .value("password_salt", ByteBuffer.wrap(passwordSalt))//Bytes.toHexString(passwordSalt))
                .value("first_name", newUser.getFirstName())
                .value("middle_name", newUser.getMiddleName())
                .value("last_name", newUser.getLastName())
                .value("phone", newUser.getPhone())
                .value("company_id", newComanyId)
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _locator.getService(CassandraConnector.class).getSession().execute(query);
        _LOG.info("New user {} inserted", newUser.getEmail());
        return newUserId.toString();
    }

    @Override
    public UserOuterClass.SystemPermission getSystemPermissions()
    {
        return null;
    }

    @Override
    public void setUserPermissions(UserOuterClass.UserPermissions userPermissions) throws Exception
    {
        Set<Integer> permissionIds = new HashSet<Integer>();
        permissionIds.addAll(userPermissions.getExtraPermissionsList());

        // Expand roles
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ROLES_TABLE)
                .where(QueryBuilder.in("role_id", userPermissions.getRolesList()));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);

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
        result = _locator.getService(CassandraConnector.class).getSession().execute(query);

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
        _locator.getService(CassandraConnector.class).getSession().execute(query);

        query = QueryBuilder.select().from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userPermissions.getUserId())));
        result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        Row userRow = result.one();
        String email = userRow.getString("email");
        query = QueryBuilder.update(KEYSPACE, USERS_TABLE).with(QueryBuilder.set("permissions", permissions))
                .where(QueryBuilder.eq("email", email));
        _locator.getService(CassandraConnector.class).getSession().execute(query);
    }

    @Override
    public UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception
    {
        // Get all premissions
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USER_PERMISSIONS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
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

    public UserOuterClass.User getUser(String email) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        Row userRow = result.one();
        if (userRow == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }

        // Populate protobuf
        return UserOuterClass.User.newBuilder()
                .setEmail(email)
                .setUserId(userRow.getUUID("user_id").toString())
                .setPhone(userRow.getString("phone"))
                .setFirstName(userRow.getString("first_name"))
                .setMiddleName(userRow.getString("middle_name"))
                .setLastName(userRow.getString("last_name"))
                .setHoldingId(userRow.getUUID("holding_id").toString())
                .setCompanyId(userRow.getUUID("company_id").toString())
                .setBranchId(userRow.getUUID("branch_id").toString())
                .addAllPermissions(userRow.getSet("permissions", String.class))
                .build();
    }

    @Override
    public void authUser(String email, String password) throws Exception
    {
        Statement query = QueryBuilder.select("password_hash", "password_salt")
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        Row row = result.one();

        if (row == null)
        {
            throw new UserNotFoundException("No user with name " + email + " found");
        }
        String passwordHash = row.getString("password_hash");
        byte[] passwordSalt = row.getBytes("password_salt").array();
        String encryptedPassword = PasswordUtil.getSecurePassword(password, passwordSalt);

        if (!encryptedPassword.equals(passwordHash))
        {
            throw new PasswordNotMatchException("Wrong password for user " + email);
        }
    }

    public List<UserOuterClass.User> getUsers()
    {
        return null;
    }

    private boolean isExists(String email)
    {
        Statement query = QueryBuilder.select()
                .countAll()
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        return (result.one().getLong("count") > 0);
    }
}
