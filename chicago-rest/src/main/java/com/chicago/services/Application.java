package com.chicago.services;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.secnod.shiro.jersey.AuthInjectionBinder;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;

public class Application extends ResourceConfig
{
    private static ServiceLocator _serviceLocator;

    public Application()
    {
        register(new ApplicationBinder());
        register(new ContainerLifecycleListener()
        {

            @Override
            public void onStartup(Container container)
            {
                // Have to have ServiceLocator because Shiro is separated from Jersey and injection
                // with annotation doesnt work
                _serviceLocator = container.getApplicationHandler().getServiceLocator();
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
        register(new AuthorizationFilterFeature());
        register(new SubjectFactory());
        register(new AuthInjectionBinder());
        packages(true, "com.chicago.services.controllers");
    }

    public static ServiceLocator getServiceLocator()
    {
        return _serviceLocator;
    }
}