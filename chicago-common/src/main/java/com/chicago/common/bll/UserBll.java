package com.chicago.common.bll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBll
{
    private final Logger _LOG = LoggerFactory.getLogger(UserBll.class);

    public void authenticate(String userName, String password)
    {
        _LOG.info("Called for user: {}", userName);
    }
}
