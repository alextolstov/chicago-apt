package com.chicago.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil
{
    public static long getUtcNowInMilliseconds()
    {
        return LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static LocalDateTime getUtcLocalDateTime(long milliseconds)
    {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
