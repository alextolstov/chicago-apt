package com.chicago.app.test;

import com.chicago.ext.dal.DbConnector;
import com.datastax.driver.core.Session;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Rule;

public class TestCassandraConnector implements DbConnector
{
    CassandraCQLUnit _cassandraCQLUnit;

    TestCassandraConnector(CassandraCQLUnit cassandraCQLUnit)
    {
        _cassandraCQLUnit = cassandraCQLUnit;
    }
//    @Inject
//    private Config.CassandraConfig cassandraConfig;

//    @PostConstruct
//    private void postConstruct()
//    {
//        connect(cassandraConfig.getNode());
//    }

    private void connect(String node)
    {
    }

    public Session getSession()
    {
        return _cassandraCQLUnit.session;
    }

    public void close()
    {
    }
}
