package com.chicago.common.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileSystemConfigAccessor extends AbstractConfigAccessor
{
    public FileSystemConfigAccessor(String path) throws Exception
    {
            open(path);
    }

    @Override
    public void open(String path) throws Exception
    {
        File f = new File(path);
        if (f.exists() && !f.isDirectory())
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                _lines.add(line);
            }

            _cgfLocation = "file://" + f.getParent();
        }
        else
        {
            throw new Exception(String.format("File %s not found.", path));
        }
    }
}
