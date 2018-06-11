package com.chicago.common.dal;

import com.chicago.dto.UserOuterClass;

import java.util.List;

public interface UserDal
{
    void registerUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception;

    UserOuterClass.User getUser();

    List<UserOuterClass.User> getUsers();
}
