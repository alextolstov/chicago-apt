package com.chicago.app;

import com.chicago.ext.bll.AddressBll;
import com.chicago.ext.bll.AddressBllImpl;
import com.chicago.ext.bll.UserBll;
import com.chicago.ext.bll.UserBllImpl;
import com.chicago.common.core.ConfigParser;
import com.chicago.ext.dal.AddressDal;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.PositionDal;
import com.chicago.ext.dal.UserDal;
import com.chicago.ext.dal.cassandra.AddressDalImpl;
import com.chicago.ext.dal.cassandra.CassandraConnector;
import com.chicago.ext.dal.cassandra.OrganizationDalImpl;
import com.chicago.ext.dal.cassandra.PositionDalImpl;
import com.chicago.ext.dal.cassandra.UserDalImpl;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Scope;
import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder
{
    private String _configFile;

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
//        addActiveDescriptor(CassandraConnector.class);
//        addActiveDescriptor(UserDalImpl.class);
        bind(CassandraConnector.class).to(CassandraConnector.class).in(Singleton.class);
        bind(UserDalImpl.class).to(UserDal.class).in(Singleton.class);
        bind(UserBllImpl.class).to(UserBll.class).in(Singleton.class);
        bind(AddressDalImpl.class).to(AddressDal.class).in(Singleton.class);
        bind(AddressBllImpl.class).to(AddressBll.class).in(Singleton.class);
        bind(PositionDalImpl.class).to(PositionDal.class).in(Singleton.class);
        bind(OrganizationDalImpl.class).to(OrganizationDal.class).in(Singleton.class);
    }
}
