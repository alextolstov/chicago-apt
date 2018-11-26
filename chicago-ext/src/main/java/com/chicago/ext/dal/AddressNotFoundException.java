package com.chicago.ext.dal;

public class AddressNotFoundException extends Exception
{
    public AddressNotFoundException(String message)
    {
        super(message);
    }
}
