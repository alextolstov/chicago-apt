package com.chicago.ext.dal.cassandra;

public class UserPermissionsNotFoundException extends Exception
{
    public UserPermissionsNotFoundException(String message)
    {
        super(message);
    }
}
