package com.chicago.common.comm;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.jvnet.hk2.annotations.Contract;

import java.util.concurrent.TimeoutException;

@Contract
public interface AsyncCommunicator
{
    byte[] transaction(Message value) throws TimeoutException, InvalidProtocolBufferException;
}
