package com.chicago.common.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourseConfigAccessor extends AbstractConfigAccessor
{
    public ResourseConfigAccessor(String path) throws Exception
    {
            open(path);
    }

    @Override
    public void open(String path) throws Exception
    {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (input != null)
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                _lines.add(line);
            }
        }
        else
        {
            throw new Exception(String.format("File %s not found.", path));
        }
    }
}
