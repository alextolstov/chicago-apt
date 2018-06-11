package com.chicago.common.dal.cassandra;

import com.chicago.common.dal.UserDal;
import com.chicago.dto.UserOuterClass;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;

import javax.inject.Inject;
import java.util.List;

import static com.chicago.common.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.common.dal.cassandra.CassandraConstants.USERS_TABLE;

public class UserDalImpl implements UserDal
{
    @Inject
    CassandraConnector connector;

    public void registerUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception
    {
        Statement query;
        query = QueryBuilder.select()
                .column("*")
                .from(KEYSPACE, USERS_TABLE);
        ResultSet result = connector.getSession().execute(query);
        if (result.one().getLong("count") > 0)
        {
            throw new Exception("User already exists");
        }
        query = QueryBuilder.insertInto(KEYSPACE, USERS_TABLE)
                .value("email", newUser.getEmail())
                .value("passwordHash", passwordHash)
                .value("passwordSalt", passwordSalt)
                .value("timeuuid", UUIDs.timeBased())
                .value("first_name", newUser.getFirstName())
                .value("middle_name", newUser.getMiddleName())
                .value("middle_name", newUser.getLastName())
                .value("phone", newUser.getPhone())
                .value("roles", newUser.getRolesList())
                .value("extra_permissions", newUser.getExtraPermissionsList());
        connector.getSession().execute(query);
    }

    public UserOuterClass.User getUser()
    {
        return null;
    }

    public List<UserOuterClass.User> getUsers()
    {
        return null;
    }
}
