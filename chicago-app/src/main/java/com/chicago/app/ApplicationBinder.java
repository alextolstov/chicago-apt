package com.chicago.app;

import com.chicago.common.core.ConfigParser;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import com.chicago.ext.bll.CityBll;
import com.chicago.ext.bll.CityBllImpl;
import com.chicago.ext.bll.SearchFiltersBll;
import com.chicago.ext.bll.SearchFiltersBllImpl;
import com.chicago.ext.dal.CityDal;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.dal.mysql.CityDalImpl;
import com.chicago.ext.dal.mysql.MySqlConnector;
import com.chicago.ext.dal.mysql.CianSearchFiltersDalImpl;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.sql.Connection;

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
        Service.ServiceConfig config;
        try
        {
            config = cp.parse(_configFile, Service.ServiceConfig.newBuilder());
            bind(config).to(Service.ServiceConfig.class);
            bind(config.getKafkaConfig()).to(Config.KafkaConfig.class);
            bind(config.getZookeeperConfig()).to(Config.ZooKeeperConfig.class);
            bind(config.getCassandraConfig()).to(Config.CassandraConfig.class);
            bind(config.getMysqlConfig()).to(Config.MySqlConfig.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
//        addActiveDescriptor(CassandraConnector.class);
//        addActiveDescriptor(UserDalImpl.class);
//        bind(CassandraConnector.class).to(DbConnector.class).in(Singleton.class);
        bind(MySqlConnector.class).to(new TypeLiteral<DbConnector<Connection>>(){}).in(Singleton.class);
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
//        bind(InventoryDalImpl.class).to(InventoryDal.class).in(Singleton.class);
//        bind(InventoryBllImpl.class).to(InventoryBll.class).in(Singleton.class);

        bind(CianSearchFiltersDalImpl.class).to(SearchFiltersDal.class).in(Singleton.class);
        bind(SearchFiltersBllImpl.class).to(SearchFiltersBll.class).in(Singleton.class);

        bind(CityDalImpl.class).to(CityDal.class).in(Singleton.class);
        bind(CityBllImpl.class).to(CityBll.class).in(Singleton.class);
    }
}
