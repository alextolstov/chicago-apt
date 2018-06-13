package com.chicago.common.dal.cassandra;

import com.chicago.common.core.AbstractComponent;
import com.chicago.common.dal.UserDal;
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
import java.util.List;
import java.util.UUID;

import static com.chicago.common.dal.cassandra.CassandraConstants.COMPANIES_TABLE;
import static com.chicago.common.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.common.dal.cassandra.CassandraConstants.USERS_TABLE;
import static java.util.Arrays.asList;

public class UserDalImpl implements UserDal
{
    private static final Logger _LOG = LoggerFactory.getLogger(AbstractComponent.class);

    private ServiceLocator _locator;

    public UserDalImpl()
    {
        _locator = ServiceLocatorFactory.getInstance().find("servicelocator");
    }

    public void registerFirstUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception
    {
        if (isExists(newUser.getEmail()))
        {
            throw new Exception("User " + newUser.getEmail() + " already exists");
        }

        UUID newComanyId = UUIDs.random();
        // Create company first
        Statement query = QueryBuilder.insertInto(KEYSPACE, COMPANIES_TABLE)
                .value("id", newComanyId)
                .value("name", "New company")
                .value("description", "New company")
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _locator.getService(CassandraConnector.class).getSession().execute(query);
        _LOG.info("New company created for user {}", newUser.getEmail());

        // Create and associate user with new company
        query = QueryBuilder.insertInto(KEYSPACE, USERS_TABLE)
                .value("id", UUIDs.random())
                .value("email", newUser.getEmail())
                .value("password_hash", passwordHash)
                .value("password_salt", ByteBuffer.wrap(passwordSalt))//Bytes.toHexString(passwordSalt))
                .value("first_name", newUser.getFirstName())
                .value("middle_name", newUser.getMiddleName())
                .value("last_name", newUser.getLastName())
                .value("phone", newUser.getPhone())
                .value("company_id", newComanyId)
                .value("roles", asList("company_admin"))
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _locator.getService(CassandraConnector.class).getSession().execute(query);
        _LOG.info("New user {} inserted", newUser.getEmail());
    }

    public void registerUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception
    {

    }

    public UserOuterClass.User getUser(String email) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new Exception("No user with name " + email + " found");
        }
        // Populate protobuf
        return UserOuterClass.User.newBuilder()
                .setEmail(email)
                .setId(result.one().getUUID("uuid").toString())
                .setPhone(result.one().getString("phone"))
                .setFirstName(result.one().getString("first_name"))
                .setMiddleName(result.one().getString("middle_name"))
                .setLastName(result.one().getString("last_name"))
                .setHoldingId(result.one().getUUID("holding_id").toString())
                .setCompanyId(result.one().getUUID("company_id").toString())
                .setBranchId(result.one().getUUID("branch_id").toString())
                .addAllRoles(result.one().getList("roles", String.class))
                .addAllExtraPermissions(row.getList("extra_permissions", String.class))
                .build();
    }

    @Override
    public boolean authUser(String email, String password) throws Exception
    {
        Statement query = QueryBuilder.select("password_hash", "password_salt")
                .from(KEYSPACE, USERS_TABLE)
                .where(QueryBuilder.eq("email", email));
        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
        Row row = result.one();

        if (row == null)
        {
            throw new Exception("No user with name " + email + " found");
        }
        String passwordHash = row.getString("password_hash");
        byte[] passwordSalt = row.getBytes("password_salt").array();
        String encryptedPassword = PasswordUtil.getSecurePassword(password, passwordSalt);

        return (encryptedPassword.equals(passwordHash));
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
