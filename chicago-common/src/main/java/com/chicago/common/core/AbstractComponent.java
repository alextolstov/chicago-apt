package com.chicago.common.core;

public abstract class AbstractComponent
{
    private String _name;

    public boolean init(ConfigAccessor ca)
    {
        return true;
    }

    public void shutdown() { }

    public void setName(String name)
    {
        _name = name;
    }

    public String getName()
    {
        return _name;
    }
}
