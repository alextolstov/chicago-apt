package com.chicago.ext.bll;

import com.chicago.dto.UserOuterClass;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface UserBll
{
    UserOuterClass.User createAdminUser(UserOuterClass.User newUser) throws Exception;

    UserOuterClass.User createStandardUser(UserOuterClass.User newUser) throws Exception;

    void setUserAvatar(UserOuterClass.UserAvatar avatar) throws Exception;

    UserOuterClass.User authUser(String userName, String password) throws Exception;

    void setUserPassword(UserOuterClass.UserPassword userPassword) throws Exception;

    List<UserOuterClass.User> getUsers(String organizationId) throws Exception;

    UserOuterClass.User getUserByEmail(String email) throws Exception;

    UserOuterClass.User getUserById(String userId) throws Exception;

    void updateUser(UserOuterClass.User user) throws Exception;
}


