package com.chicago.ext.test;

import com.chicago.common.util.TimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class TestTime
{
    //@DisplayName("Test not to milliseconds and check if it is correct")
    @Test
    public void compareSystemAndProtoTimestamp()
    {
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        long now = TimeUtil.getUtcNowInMilliseconds();
        String revertedTime = TimeUtil.getUtcLocalDateTime(now).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertEquals(nowTime, revertedTime);
    }
}
