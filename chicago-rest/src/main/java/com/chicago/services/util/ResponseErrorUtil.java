package com.chicago.services.util;

import com.chicago.dto.Common;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.ws.rs.core.Response;

public class ResponseErrorUtil
{
    public static Response createErrorResponse(String error, int errorCode)
    {
        Common.TransactionError transactionError = Common.TransactionError.newBuilder()
                .setErrorCode(errorCode)
                .setErrorMessage(error)
                .build();
        String jsonError = null;
        try
        {
            jsonError = JsonFormat.printer().print(transactionError);
        } catch (InvalidProtocolBufferException e1)
        {
            e1.printStackTrace();
        }
        return Response.serverError().entity(jsonError).type("application/json").build();
    }
}
