package com.chicago.ext.bll;

import com.chicago.dto.UserOuterClass;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserBll
{
    UserOuterClass.User createAdminUser(UserOuterClass.User newUser) throws Exception;

    UserOuterClass.User createStandardUser(UserOuterClass.User newUser) throws Exception;

    void authUser(String userName, String password) throws Exception;
}
