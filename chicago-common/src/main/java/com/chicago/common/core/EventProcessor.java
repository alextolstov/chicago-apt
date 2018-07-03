package com.chicago.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventProcessor
{
    private static final Logger _LOG = LoggerFactory.getLogger(EventProcessor.class);
    private static final int _THREADS = 30;

    private HashMap<String, HashSet<EventHandler>> _eventHandlers = new HashMap<>();
    private long _eventCount = 0;
    private ExecutorService _executor = Executors.newFixedThreadPool(_THREADS);

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
                // Start and forget, internally it will push response into the queue
                _executor.execute(() -> eh.handleEvent(event.getMessage(), event.getTransactionId()));
                _LOG.info("Task with transaction id: {} was submitted and message type: {}", event.getTransactionId(), event.getMessage().getClass());
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

    public <T> void registerHandler(Class clazz, EventHandler<T> handler)
    {
        HashSet<EventHandler> handlers;
        String typeId = clazz.getCanonicalName();

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
