package com.chicago.common.dal.cassandra;

public class UserNotFoundException extends Exception
{
    public UserNotFoundException(String message)
    {
        super(message);
    }
}
