package com.chicago.app.test;

import com.chicago.ext.bll.*;
import com.chicago.ext.dal.*;
import com.chicago.ext.dal.cassandra.AddressDalImpl;
import com.chicago.ext.dal.cassandra.InventoryDalImpl;
import com.chicago.ext.dal.cassandra.OrganizationDalImpl;
import com.chicago.ext.dal.cassandra.PermissionDalImpl;
import com.chicago.ext.dal.cassandra.PositionDalImpl;
import com.chicago.ext.dal.cassandra.UserDalImpl;
import com.chicago.ext.dal.mysql.CianSearchFiltersDalImpl;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.sql.Connection;

public class TestMySqlBinder extends AbstractBinder
{
//    private String _configFile;
    TestMySqlConnector _ts;

    public TestMySqlBinder(TestMySqlConnector ts)
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
        bind(_ts).to(new TypeLiteral<DbConnector<Connection>>(){});//.in(Singleton.class);
//        bind(UserDalImpl.class).to(UserDal.class).in(Singleton.class);
//        bind(UserBllImpl.class).to(UserBll.class).in(Singleton.class);
//
//        bind(AddressDalImpl.class).to(AddressDal.class).in(Singleton.class);
//        bind(AddressBllImpl.class).to(AddressBll.class).in(Singleton.class);
//
//        bind(PositionDalImpl.class).to(PositionDal.class).in(Singleton.class);
//        bind(PositionBllImpl.class).to(PositionBll.class).in(Singleton.class);
//
//        bind(OrganizationDalImpl.class).to(OrganizationDal.class).in(Singleton.class);
//        bind(OrganizationBllImpl.class).to(OrganizationBll.class).in(Singleton.class);
//
//        bind(PermissionDalImpl.class).to(PermissionDal.class).in(Singleton.class);
//        bind(PermissionBllImpl.class).to(PermissionBll.class).in(Singleton.class);
//
        bind(InventoryDalImpl.class).to(InventoryDal.class).in(Singleton.class);
        bind(InventoryBllImpl.class).to(InventoryBll.class).in(Singleton.class);

        bind(CianSearchFiltersDalImpl.class).to(SearchFiltersDal.class).in(Singleton.class);
        bind(SearchFiltersBllImpl.class).to(SearchFiltersBll.class).in(Singleton.class);
    }
}
