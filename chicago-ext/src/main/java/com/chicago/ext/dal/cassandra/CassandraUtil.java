package com.chicago.ext.dal.cassandra;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CassandraUtil
{
    public static Set<UUID> convertToUuidSet(Collection<String> stringSet)
    {
        final Set<UUID> newSet = new HashSet<>();
        if (stringSet != null)
        {
            stringSet.stream().map(UUID::fromString).forEach(entry -> newSet.add(entry));
        }
        return newSet;
    }

    public static Set<UUID> convertToUuidSet(Map<String, String> stringMap)
    {
        final Set<UUID> newSet = new HashSet<>();
        if (stringMap != null)
        {
            stringMap.entrySet().forEach(entry -> newSet.add(UUID.fromString(entry.getKey())));
        }
        return newSet;
    }

    public static Map<UUID, String> convertToUuidMap(Map<String, String> stringMap)
    {
        final Map<UUID, String> newMap = new HashMap<>();

        if (stringMap != null)
        {
            stringMap.entrySet().forEach(entry -> newMap.put(UUID.fromString(entry.getKey()), entry.getValue()));
        }
        return newMap;
    }

}
