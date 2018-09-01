package com.chicago.ext.dal;

import com.chicago.dto.UserOuterClass;

import java.util.List;

public interface UserDal
{
    String createUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt)
            throws Exception;

    boolean isUserExists(String email);

    void setUserPermissions(UserOuterClass.UserPermissions permissions) throws Exception;

    UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception;

    void authUser(String email, String password) throws Exception;

    UserOuterClass.User getUser(String email) throws Exception;

    List<UserOuterClass.User> getUsers() throws Exception;
}
