package com.chicago.common.util;

import com.chicago.common.core.LazyLiveEventDispatcher;
import com.chicago.dto.Usermessages;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseFactoryUtil
{
    private static final Logger _LOG = LoggerFactory.getLogger(ResponseFactoryUtil.class);

    public static Message createErrorResponse(String error,int errorCode, Class type)
    {
        _LOG.error(error);

        if (type.getCanonicalName().equals(Usermessages.CreateUserResponse.class.getCanonicalName()))
        {
            return Usermessages.CreateUserResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, error))
                    .build();
        }
        if (type.getCanonicalName().equals(Usermessages.LoginUserResponse.class.getCanonicalName()))
        {
            return Usermessages.LoginUserResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, error))
                    .build();
        }

        _LOG.error("Unknown message type: {}", Usermessages.CreateUserResponse.class.getCanonicalName());
        return null;
    }
}
