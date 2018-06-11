package com.chicago.common.bll;

import com.chicago.common.dal.UserDal;
import com.chicago.common.dal.cassandra.UserDalImpl;
import com.chicago.common.util.PasswordUtil;
import com.chicago.dto.UserOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBll
{
    private final Logger _LOG = LoggerFactory.getLogger(UserBll.class);

    UserDal userDal;

    public void createUser(UserOuterClass.User newUser)
    {
        try
        {
            byte[] passwordSalt = PasswordUtil.getSalt();
            String passwordHash = PasswordUtil.getSecurePassword(newUser.getPassword(), passwordSalt);
            userDal.registerUser(newUser, passwordHash, passwordSalt);
        } catch (Exception ex)
        {

        }
    }

    public void authenticate(String userName, String password)
    {
        _LOG.info("Called for user: {}", userName);
    }
}
