package com.chicago.ext.dal;

import com.datastax.driver.core.Session;

public interface DbConnector
{
    public Session getSession();

    public void close();
}
