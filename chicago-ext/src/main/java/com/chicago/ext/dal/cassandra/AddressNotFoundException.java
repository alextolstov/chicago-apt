package com.chicago.ext.dal.cassandra;

public class AddressNotFoundException extends Exception
{
    public AddressNotFoundException(String message)
    {
        super(message);
    }
}
