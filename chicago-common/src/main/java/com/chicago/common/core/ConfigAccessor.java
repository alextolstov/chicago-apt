package com.chicago.common.core;

import com.google.protobuf.*;
import com.google.protobuf.Descriptors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigAccessor
{
    private static final Logger _LOG = LoggerFactory.getLogger(ConfigAccessor.class);

    public ConfigAccessor(Message config)
    {
        _globalConfig = config;
    }

    public <T extends Message> T getGlobalConfig()
    {
        return (T) _globalConfig;
    }

    public <T extends Message> T getConfig(String findFieldName)
    {
        Descriptor d = _globalConfig.getDescriptorForType();
        List<FieldDescriptor> lst = d.getFields();
        for (FieldDescriptor fd : lst)
        {
            try
            {
                Descriptor d2 = fd.getMessageType();
                String str = d2.getFullName();
                if (str.equals(findFieldName))
                {
                    return (T) _globalConfig.getField(fd);
                }
            } catch (UnsupportedOperationException ex) // It is ok to have this exception, not any fields allow getMessageType()
            {
                _LOG.info("Exception in ConfigAccessor. It is expected. Ignore it.");
            }
        }
        return null;
    }

    private Message _globalConfig;
}
