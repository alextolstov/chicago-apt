package com.chicago.common.dal.cassandra;

import com.chicago.common.dal.UserDal;
import com.chicago.dto.UserOuterClass;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.List;

import static com.chicago.common.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.common.dal.cassandra.CassandraConstants.USERS_TABLE;

public class UserDalImpl implements UserDal
{
    CassandraConnector connector;

    public void registerUser(UserOuterClass.User newUser)
    {
        Statement query = QueryBuilder.insertInto(KEYSPACE, USERS_TABLE)
                .value("phone", newUser.getPhone())
                .value("email", newUser.getEmail());
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
