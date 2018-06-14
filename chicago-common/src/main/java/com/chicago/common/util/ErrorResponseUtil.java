package com.chicago.common.util;

import com.chicago.dto.Common;

public class ErrorResponseUtil
{
    public static Common.TransactionError createErrorResponse(int errorCode, String errorText)
    {
        return Common.TransactionError
                .newBuilder()
                .setErrorCode(errorCode)
                .setErrorMessage(errorText)
                .build();
    }
}
