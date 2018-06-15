package com.chicago.common.bll;

import com.chicago.dto.UserOuterClass;

public interface UserBll
{
    void createFirstUser(UserOuterClass.User newUser) throws Exception;

    void authUser(String userName, String password) throws Exception;
}
