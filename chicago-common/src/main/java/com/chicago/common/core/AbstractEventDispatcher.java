package com.chicago.common.core;


import com.google.protobuf.Message;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEventDispatcher extends AbstractComponent
{
    protected EventProcessor _eventProcessor = new EventProcessor();
    private Map<String, Class> _typeToClass = new HashMap<String, Class>();

    public abstract void publishRealTimeEvent(EventBase event);

    public abstract void scheduleFutureEvent(EventBase event, boolean ignoreCheck);

    public Message deserializeMessage(String typeId, byte[] serializedMessage)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Class clazz = _typeToClass.get(typeId);
        if (clazz == null)
        {
            throw new NoSuchMethodException("Type " + typeId + " not found in deserializer");
        }
        return (Message) clazz.getMethod("parseFrom", byte[].class).invoke(clazz, serializedMessage);
    }

    public <T> void registerHandler(Class clazz, EventHandler<T> handler)
    {
        _typeToClass.put(clazz.getCanonicalName(), clazz);
        _eventProcessor.registerHandler(clazz, handler);
    }

    public abstract void run();

    public abstract int queueCount();

    public abstract long processedEvents();
}
