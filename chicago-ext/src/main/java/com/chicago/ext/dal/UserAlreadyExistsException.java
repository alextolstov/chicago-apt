package com.chicago.ext.dal;

public class UserAlreadyExistsException extends Exception
{
    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
