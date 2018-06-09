package com.chicago.services;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig
{
    public Application()
    {
        register(new ApplicationBinder());
        packages(true, "com.chicago.services.controllers");
    }
}