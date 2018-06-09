package com.chicago.common.core;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FutureEventScheduler
{
    private final Lock _lock = new ReentrantLock();
    private final Condition _cv = _lock.newCondition();
    private PriorityQueue<EventBase> _events =
            new PriorityQueue<EventBase>((EventBase e1, EventBase e2)->{return e1.getTimestamp().compareTo(e2.getTimestamp());});
    private ConcurrentLinkedQueue<EventBase> _unscheduledEventsQueue = new ConcurrentLinkedQueue<EventBase>();

    public void run()
    {
        for (;;)
        {
            EventBase event;

            while ((event = _unscheduledEventsQueue.poll()) != null)
            {
                _events.add(event);
            }
        }
    }

    void scheduleEvent(EventBase event)
    {
        _lock.lock();
        while (!_unscheduledEventsQueue.add(event))
        {
            ;
        }
        _cv.signal();
    }
}
