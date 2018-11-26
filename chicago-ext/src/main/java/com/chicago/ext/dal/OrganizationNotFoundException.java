package com.chicago.ext.dal;

public class OrganizationNotFoundException extends Exception
{
    public OrganizationNotFoundException(String message)
    {
        super(message);
    }
}
