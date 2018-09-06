package com.chicago.ext.dal;

import com.chicago.dto.UserOuterClass;

import java.util.List;

public interface UserDal
{
    String createUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt)
            throws Exception;

    boolean isUserExists(String email);

    void updateUser(UserOuterClass.User user) throws Exception;

    void setUserPermissions(UserOuterClass.UserPermissions permissions) throws Exception;

    void setUserAvatar(UserOuterClass.UserAvatar avatar) throws Exception;

    UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception;

    void authUser(String email, String password) throws Exception;

    UserOuterClass.User getUser(String email) throws Exception;

    List<UserOuterClass.User> getUsers() throws Exception;
}
