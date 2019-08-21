package com.chicago.app.test;

import com.chicago.common.core.ConfigParser;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import com.chicago.ext.bll.AddressBll;
import com.chicago.ext.bll.AddressBllImpl;
import com.chicago.ext.bll.InventoryBll;
import com.chicago.ext.bll.InventoryBllImpl;
import com.chicago.ext.bll.OrganizationBll;
import com.chicago.ext.bll.OrganizationBllImpl;
import com.chicago.ext.bll.PermissionBll;
import com.chicago.ext.bll.PermissionBllImpl;
import com.chicago.ext.bll.PositionBll;
import com.chicago.ext.bll.PositionBllImpl;
import com.chicago.ext.bll.UserBll;
import com.chicago.ext.bll.UserBllImpl;
import com.chicago.ext.dal.AddressDal;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.InventoryDal;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.PermissionDal;
import com.chicago.ext.dal.PositionDal;
import com.chicago.ext.dal.UserDal;
import com.chicago.ext.dal.cassandra.AddressDalImpl;
import com.chicago.ext.dal.cassandra.InventoryDalImpl;
import com.chicago.ext.dal.cassandra.OrganizationDalImpl;
import com.chicago.ext.dal.cassandra.PermissionDalImpl;
import com.chicago.ext.dal.cassandra.PositionDalImpl;
import com.chicago.ext.dal.cassandra.UserDalImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class TestCassandraBinder extends AbstractBinder
{
//    private String _configFile;
    TestCassandraConnector _ts;

    public TestCassandraBinder(TestCassandraConnector ts)
    {
        _ts = ts;
  //      _configFile = configFile;
    }

    @Override
    protected void configure()
    {
//        ConfigParser cp = new ConfigParser();
//        Service.ServiceConfig config;
//        try
//        {
//            config = cp.parse(_configFile, Service.ServiceConfig.newBuilder());
//            bind(config).to(Service.ServiceConfig.class);
//            bind(config.getKafkaConfig()).to(Config.KafkaConfig.class);
//            bind(config.getZookeeperConfig()).to(Config.ZooKeeperConfig.class);
//            bind(config.getCassandraConfig()).to(Config.CassandraConfig.class);
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        addActiveDescriptor(CassandraConnector.class);
//        addActiveDescriptor(UserDalImpl.class);
        bind(_ts).to(DbConnector.class);//.in(Singleton.class);
        bind(UserDalImpl.class).to(UserDal.class).in(Singleton.class);
        bind(UserBllImpl.class).to(UserBll.class).in(Singleton.class);
        bind(AddressDalImpl.class).to(AddressDal.class).in(Singleton.class);
        bind(AddressBllImpl.class).to(AddressBll.class).in(Singleton.class);
        bind(PositionDalImpl.class).to(PositionDal.class).in(Singleton.class);
        bind(PositionBllImpl.class).to(PositionBll.class).in(Singleton.class);
        bind(OrganizationDalImpl.class).to(OrganizationDal.class).in(Singleton.class);
        bind(OrganizationBllImpl.class).to(OrganizationBll.class).in(Singleton.class);
        bind(PermissionDalImpl.class).to(PermissionDal.class).in(Singleton.class);
        bind(PermissionBllImpl.class).to(PermissionBll.class).in(Singleton.class);
        bind(InventoryDalImpl.class).to(InventoryDal.class).in(Singleton.class);
        bind(InventoryBllImpl.class).to(InventoryBll.class).in(Singleton.class);
    }
}
