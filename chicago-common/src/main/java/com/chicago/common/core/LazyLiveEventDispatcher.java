package com.chicago.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyLiveEventDispatcher extends AbstractEventDispatcher
{
    private static final Logger LOG = LoggerFactory.getLogger(LazyLiveEventDispatcher.class);

    private final Lock _lock = new ReentrantLock();
    private final Condition _cv = _lock.newCondition();
    private AtomicBoolean _isShutdown = new AtomicBoolean(false);
    private AtomicInteger _queueCount = new AtomicInteger(0);
    private AtomicLong _eventCount = new AtomicLong(0);
    private ConcurrentLinkedQueue<EventBase> _queue = new ConcurrentLinkedQueue<>();
    private Thread _thr;
    private FutureEventScheduler _eventScheduler;

    public LazyLiveEventDispatcher(ComponentManager cm)
    {
        _eventScheduler = new FutureEventScheduler();
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    @Override
    public void publishRealTimeEvent(EventBase event)
    {
        if(event == null)
        {
            LOG.warn("Null event is ignored");
            return;
        }

        _queueCount.incrementAndGet();
        while (!_isShutdown.get() && !_queue.add(event));

        _lock.lock();
        try
        {
            _cv.signal();
        } finally
        {
            _lock.unlock();
        }
    }

    @Override
    public void scheduleFutureEvent(EventBase event, boolean ignoreCheck)
    {
        if (_isShutdown.get())
            return;
        _eventScheduler.scheduleEvent(event);
    }

    @Override
    public void run()
    {
        _thr = new Thread(() -> _eventScheduler.run());
        _thr.start();

        while (!_isShutdown.get())
        {
            EventBase event;
            while ((event = _queue.poll()) != null)
            {
                if (_eventCount.incrementAndGet() % 100 == 0)
                {
                    LOG.debug("Event count: {}", _eventCount.get());
                }
                _queueCount.decrementAndGet();
                _eventProcessor.processEvent(event);
            }

            _lock.lock();
            try
            {
                if (_queue.isEmpty())
                {
                    _cv.await(100, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e)
            {
                LOG.error(e.getStackTrace().toString());
            } finally
            {
                _lock.unlock();
            }
        }
    }

    @Override
    public int queueCount()
    {
        return _queueCount.get();
    }

    @Override
    public long processedEvents()
    {
        return _eventCount.get();
    }
}
