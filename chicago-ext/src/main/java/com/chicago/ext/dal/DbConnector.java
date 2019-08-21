package com.chicago.ext.dal;

public interface DbConnector<T>
{
    public T getSession();

    public void close();
}
