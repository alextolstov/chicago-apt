package com.chicago.ext.util;

import com.chicago.common.util.ErrorResponseUtil;
import com.chicago.dto.Common;
import com.chicago.dto.Usermessages;
import com.chicago.ext.dal.PasswordNotMatchException;
import com.chicago.ext.dal.UserAlreadyExistsException;
import com.chicago.ext.dal.UserNotFoundException;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseFactoryUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(ResponseFactoryUtil.class);

    public static Message createErrorResponse(Exception ex, Class returnType)
    {
        int errorCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;

        LOG.error("{}", ex);
        if (ex instanceof UserAlreadyExistsException)
        {
            errorCode = HttpStatus.SC_PRECONDITION_FAILED;
        }
        else if (ex instanceof UserNotFoundException)
        {
            errorCode = HttpStatus.SC_NOT_FOUND;
        }
        else if (ex instanceof PasswordNotMatchException)
        {
            errorCode = HttpStatus.SC_UNAUTHORIZED;
        }

        if (returnType.getCanonicalName().equals(Usermessages.UserResponse.class.getCanonicalName()))
        {
            return Usermessages.UserResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, ex.getMessage()))
                    .build();
        }
        if (returnType.getCanonicalName().equals(Common.VoidResponse.class.getCanonicalName()))
        {
            return Common.VoidResponse
                    .newBuilder()
                    .setTransactionError(ErrorResponseUtil.createErrorResponse(errorCode, ex.getMessage()))
                    .build();
        }

        LOG.error("Unknown message type: {}", returnType.getCanonicalName());
        return null;
    }
}
