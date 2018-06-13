package com.chicago.common.bll;

import com.chicago.dto.UserOuterClass;

public interface UserBll
{
    void createFirstUser(UserOuterClass.User newUser) throws Exception;

    void authenticate(String userName, String password);
}
