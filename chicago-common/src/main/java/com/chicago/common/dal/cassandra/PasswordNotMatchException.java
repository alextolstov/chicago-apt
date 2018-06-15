package com.chicago.common.dal.cassandra;

public class PasswordNotMatchException extends Exception
{
    public PasswordNotMatchException(String message)
    {
        super(message);
    }
}
