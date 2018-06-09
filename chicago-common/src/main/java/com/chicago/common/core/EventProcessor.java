package com.chicago.common.core;

import com.chicago.common.bll.UserBll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;

public class EventProcessor
{
    private final Logger _LOG = LoggerFactory.getLogger(UserBll.class);
    private HashMap<String, HashSet<EventHandler>> _eventHandlers = new HashMap<String, HashSet<EventHandler>>();
    private long _eventCount = 0;

    /**
     * Broadcast event to all subscribers
     * @param event
     */
    public void processEvent(EventBase event)
    {
        HashSet<EventHandler> handlers = _eventHandlers.get(event.getName());
        if (handlers != null)
        {
            for (EventHandler eh : handlers)
            {
                eh.handleEvent(event.getMessage(), event.getTransactionId());
            }
        }
        else
        {
            _LOG.error("Event handler for event {} not found", event.getName());
        }
    }

    public void logStats()
    {

    }

    public <T> void registerHandler(String typeId, EventHandler<T> handler)
    {
        HashSet<EventHandler> handlers = null;

        if (!_eventHandlers.containsKey(typeId))
        {
            handlers = new HashSet<EventHandler>();
            _eventHandlers.put(typeId, handlers);
        }else
        {
            handlers = _eventHandlers.get(typeId);
        }
        handlers.add(handler);
    }
}
