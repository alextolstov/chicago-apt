package com.chicago.app;

import com.chicago.common.core.ConfigParser;
import com.chicago.common.dal.cassandra.CassandraConnector;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder
{
    String _configFile;

    public ApplicationBinder(String configFile)
    {
        _configFile = configFile;
    }

    @Override
    protected void configure()
    {
        ConfigParser cp = new ConfigParser();
        Service.ServiceConfig config = null;
        try
        {
            config = cp.parse(_configFile, Service.ServiceConfig.newBuilder());
            bind(config).to(Service.ServiceConfig.class);
            bind(config.getKafkaConfig()).to(Config.KafkaConfig.class);
            bind(config.getZookeeperConfig()).to(Config.ZooKeeperConfig.class);
            bind(config.getCassandraConfig()).to(Config.CassandraConfig.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        addActiveDescriptor(CassandraConnector.class);
    }
}
