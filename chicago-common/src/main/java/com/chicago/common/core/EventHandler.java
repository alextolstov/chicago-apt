package com.chicago.common.core;

public interface EventHandler<T>
{
    void handleEvent(T event, String transactionId);
}
