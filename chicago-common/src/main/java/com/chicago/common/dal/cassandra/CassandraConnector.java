package com.chicago.common.dal.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector
{
    private Cluster _cluster;
    private Session _session;

    public void connect(String node, Integer port)
    {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null)
        {
            b.withPort(port);
        }
        _cluster = b.build();

        _session = _cluster.connect();
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
