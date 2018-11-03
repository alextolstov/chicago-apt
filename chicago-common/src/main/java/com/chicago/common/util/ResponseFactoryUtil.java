package com.chicago.common.util;

import com.chicago.dto.Common;
import com.chicago.dto.Usermessages;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseFactoryUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(ResponseFactoryUtil.class);

    public static Message createErrorResponse(String error, int errorCode, Class type)
    {
        LOG.error(error);

        if (type.getCanonicalName().equals(Usermessages.UserResponse.class.getCanonicalName()))
        {
            return Usermessages.UserResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, error))
                    .build();
        }
        if (type.getCanonicalName().equals(Common.VoidResponse.class.getCanonicalName()))
        {
            return Common.VoidResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, error))
                    .build();
        }
        if (type.getCanonicalName().equals(Common.VoidResponse.class.getCanonicalName()))
        {
            return Common.VoidResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, error))
                    .build();
        }

        LOG.error("Unknown message type: {}", type.getCanonicalName());
        return null;
    }
}
