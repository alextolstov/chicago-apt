package com.chicago.common.core;

import com.google.protobuf.Message;

import java.time.LocalDateTime;

public class EventBase
{
    private LocalDateTime _ts;
    private Message _protoMsg;
    private String _typeId;
    private String _transactionId;

    public EventBase(LocalDateTime ts, Message protoMsg, String transacionId)
    {
        _ts = ts;
        _protoMsg = protoMsg;
        _typeId = _protoMsg.getClass().getCanonicalName();
        _transactionId = transacionId;
    }

    public LocalDateTime getTimestamp()
    {
        return _ts;
    }

    public String getName()
    {
        return _typeId;
    }

    public Message getMessage()
    {
        return _protoMsg;
    }

    public String getTransactionId()
    {
        return _transactionId;
    }
}
