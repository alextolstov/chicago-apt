package com.chicago.ext.dal.cassandra;

import com.chicago.dto.Config;
import com.chicago.ext.dal.DbConnector;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class CassandraConnector implements DbConnector<Session>
{
    private static final Logger LOG = LoggerFactory.getLogger(CassandraConnector.class);
    private Cluster _cluster;
    private Session _session;

    @Inject
    private Config.CassandraConfig cassandraConfig;

    @PostConstruct
    private void postConstruct()
    {
        connect(cassandraConfig.getNode());
    }

    private void connect(String node)
    {
        String[] stringArray = node.split(":");
        String server = stringArray[0];

        Cluster.Builder b = Cluster.builder().addContactPoint(server);
        if (stringArray.length == 2)
        {
            b.withPort(Integer.parseInt(stringArray[1]));
        }
        _cluster = b.build();

        _session = _cluster.connect();
        LOG.info("Connected to Cassandra cluster: {}", node);
    }

    public Session getSession()
    {
        return _session;
    }

    public void close()
    {
        _session.close();
        _cluster.close();
    }
}
