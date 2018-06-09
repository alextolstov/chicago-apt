package com.chicago.services;

import com.chicago.common.comm.KafkaAsyncCommunicator;
import com.chicago.common.core.ConfigParser;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import com.chicago.services.controllers.AuthController;
import com.chicago.services.controllers.UserController;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder
{
    @Override
    protected void configure()
    {
        String confName="res://config/restservice.cfg";
        ConfigParser cp = new ConfigParser();
        Service.RestServiceConfig config = null;
        try
        {
            config = cp.parse(confName, Service.RestServiceConfig.newBuilder());
            bind(config).to(Service.RestServiceConfig.class);
            bind(config.getKafkaConfig()).to(Config.KafkaConfig.class);
            bind(config.getZookeeperConfig()).to(Config.ZooKeeperConfig.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        addActiveDescriptor(AuthController.class);
        addActiveDescriptor(UserController.class);
        addActiveDescriptor(KafkaAsyncCommunicator.class);
    }
}
