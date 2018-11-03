package com.chicago.app;

import com.chicago.common.components.kafka.KafkaMessageConsumer;
import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.Application;
import com.chicago.common.core.LazyLiveEventDispatcher;
import com.chicago.ext.components.AddressRequests;
import com.chicago.ext.components.PermissionRequests;
import com.chicago.ext.components.PositionRequests;
import com.chicago.ext.components.UserRequests;
import com.google.protobuf.GeneratedMessageV3;

public class ServiceEntry<E, T extends GeneratedMessageV3.Builder<T>> extends Application
{
    public ServiceEntry(String confName, String instanceId) throws Exception
    {
        super(confName, instanceId);
        KafkaMessageConsumer.registerComponentFactories();
        KafkaMessageProducer.registerComponentFactories();
        UserRequests.registerComponentFactories();
        AddressRequests.registerComponentFactories();
        PositionRequests.registerComponentFactories();
        PermissionRequests.registerComponentFactories();
        LazyLiveEventDispatcher.registerComponentFactories();
    }

    public void run() throws ClassNotFoundException
    {
        String cls = AbstractEventDispatcher.class.getCanonicalName();
        AbstractEventDispatcher ad = getComponentManager().getResource(cls);
        ad.run();
    }
}
