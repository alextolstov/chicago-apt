package com.chicago.common.bll;

import com.chicago.common.dal.UserDal;
import com.chicago.common.util.PasswordUtil;
import com.chicago.dto.UserOuterClass;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserBllImpl implements UserBll
{
    private static final Logger _LOG = LoggerFactory.getLogger(UserBllImpl.class);
    private ServiceLocator _locator;

    public UserBllImpl()
    {
        _locator = ServiceLocatorFactory.getInstance().find("servicelocator");
    }

    public void setUserPermissions(String userId, List<Integer> roles, List<Integer> extraPermissions) throws Exception
    {
        if (roles == null)
        {
            roles = new ArrayList<>();
        }
        if (extraPermissions == null)
        {
            extraPermissions = new ArrayList<>();
        }
        UserOuterClass.UserPermissions permissions = UserOuterClass.UserPermissions.newBuilder()
                .setUserId(userId)
                .addAllRoles(roles)
                .addAllExtraPermissions(extraPermissions)
                .build();
        _locator.getService(UserDal.class).setUserPermissions(permissions);
    }

    public String createAdminUser(UserOuterClass.User newUser) throws Exception
    {
        String userId = createStandardUser(newUser);
        setUserPermissions(userId, Arrays.asList(0), null);
        return userId;
    }

    public String createStandardUser(UserOuterClass.User newUser) throws Exception
    {
        byte[] passwordSalt = PasswordUtil.getSalt();
        String passwordHash = PasswordUtil.getSecurePassword(newUser.getPassword(), passwordSalt);
        return _locator.getService(UserDal.class).registerUser(newUser, passwordHash, passwordSalt);
    }

    public void authUser(String userName, String password) throws Exception
    {
        _LOG.info("Called for user: {}", userName);
        _locator.getService(UserDal.class).authUser(userName, password);
    }
}
