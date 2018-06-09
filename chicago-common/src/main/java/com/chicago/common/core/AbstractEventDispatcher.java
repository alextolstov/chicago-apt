package com.chicago.common.core;


public abstract class AbstractEventDispatcher extends AbstractComponent
{
    protected EventProcessor _eventProcessor = new EventProcessor();

    public abstract void publishRealTimeEvent(EventBase event);

    public abstract void scheduleFutureEvent(EventBase event, boolean ignoreCheck);

    public <T> void registerHandler(String typeId, EventHandler<T> handler)
    {
        _eventProcessor.registerHandler(typeId, handler);
    }

    public abstract void run();

    public abstract int queueCount();

    public abstract long processedEvents();
}
