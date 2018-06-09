package com.chicago.common.core;

public class ConfigAccessorFactory
{
    public static AbstractConfigAccessor createConfigAccessor(String path) throws Exception
    {
        if (path.toLowerCase().contains("zoo://"))
        {
            return new ZooKeeperConfigAccessor(path.substring("zoo://".length()));
        }
        else if (path.toLowerCase().contains("file://"))
        {
            return new FileSystemConfigAccessor(path.substring("file://".length()));
        }
        else if (path.toLowerCase().contains("res://"))
        {
            return new ResourseConfigAccessor(path.substring("res://".length()));
        }
        else
        {
            throw new Exception("Unknown configuration URI schema. use zoo:// for Zookeeper or file:// for local file.");
        }
    }
}
