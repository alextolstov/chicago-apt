package com.chicago.ext.dal;

public class PasswordNotMatchException extends Exception
{
    public PasswordNotMatchException(String message)
    {
        super(message);
    }
}
