package com.chicago.app;

import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.Application;
import com.google.protobuf.GeneratedMessageV3;

public class ServiceEntry<E, T extends GeneratedMessageV3.Builder<T>> extends Application
{
    public ServiceEntry(String confName, String instanceId) throws Exception
    {
        super(confName, instanceId);
    }

    public void run() throws ClassNotFoundException
    {
        String cls = AbstractEventDispatcher.class.getCanonicalName();
        AbstractEventDispatcher ad = getComponentManager().getResource(cls);
        ad.run();
    }
}
