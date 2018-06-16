package com.chicago.services;

import com.chicago.common.comm.AsyncCommunicator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

public class Application extends ResourceConfig
{
    private static InjectionManager _serviceLocator;

    public Application()
    {
        register(new ApplicationBinder());
        register(new ContainerLifecycleListener()
        {

            @Override
            public void onStartup(Container container)
            {
                _serviceLocator = container.getApplicationHandler().getInjectionManager();
//                AsyncCommunicator asyncCommunicator =_serviceLocator.getInstance(AsyncCommunicator.class);
            }

            @Override
            public void onReload(Container container)
            {

            }

            @Override
            public void onShutdown(Container container)
            {

            }
        });
        packages(true, "com.chicago.services.controllers");
    }

    public static InjectionManager getServiceLocator()
    {
        return _serviceLocator;
    }
}