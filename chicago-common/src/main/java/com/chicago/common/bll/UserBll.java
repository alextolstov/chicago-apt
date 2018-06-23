package com.chicago.common.bll;

import com.chicago.dto.UserOuterClass;

public interface UserBll
{
    String createAdminUser(UserOuterClass.User newUser) throws Exception;

    String createStandardUser(UserOuterClass.User newUser) throws Exception;

    void authUser(String userName, String password) throws Exception;
}
