package com.chicago.common.bll;

import com.chicago.common.dal.UserDal;
import com.chicago.common.util.PasswordUtil;
import com.chicago.dto.UserOuterClass;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBllImpl implements UserBll
{
    private final Logger _LOG = LoggerFactory.getLogger(UserBllImpl.class);
    private ServiceLocator _locator;

    public UserBllImpl()
    {
        _locator = ServiceLocatorFactory.getInstance().find("servicelocator");
    }

    public void createFirstUser(UserOuterClass.User newUser) throws Exception
    {
        byte[] passwordSalt = PasswordUtil.getSalt();
        String passwordHash = PasswordUtil.getSecurePassword(newUser.getPassword(), passwordSalt);
        _locator.getService(UserDal.class).registerFirstUser(newUser, passwordHash, passwordSalt);
    }

    public void authUser(String userName, String password) throws Exception
    {
        _LOG.info("Called for user: {}", userName);
        _locator.getService(UserDal.class).authUser(userName, password);
    }
}
