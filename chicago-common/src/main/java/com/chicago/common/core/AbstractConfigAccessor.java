package com.chicago.common.core;

import java.util.ArrayList;

public abstract class AbstractConfigAccessor
{
    protected int _currentLine = 0;
    protected ArrayList<String> _lines = new ArrayList<>();
    protected String _cgfLocation;

    public String getNextLine()
    {
        if (_currentLine >= _lines.size())
            return null;
        return _lines.get(_currentLine++);
    }

    public abstract void open(String path) throws Exception;

    public String getCfgLocation()
    {
        return _cgfLocation;
    }
}
