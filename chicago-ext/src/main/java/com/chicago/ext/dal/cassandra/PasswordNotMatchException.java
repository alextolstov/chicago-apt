package com.chicago.ext.dal.cassandra;

public class PasswordNotMatchException extends Exception
{
    public PasswordNotMatchException(String message)
    {
        super(message);
    }
}
