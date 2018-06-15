package com.chicago.common.dal;

import com.chicago.dto.UserOuterClass;

import java.util.List;

public interface UserDal
{
    /**
     * Create user and setup the new company. User added as admin to the new company
     * As next step this user will add new users and create branches or holding
     * @param newUser
     * @param passwordHash
     * @param passwordSalt
     * @throws Exception
     */
    void registerFirstUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception;

    /**
     * Create user in existing holding/company/branch
     * @param newUser
     * @param passwordHash
     * @param passwordSalt
     * @throws Exception
     */
    void registerUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt) throws Exception;

    void authUser(String email, String password) throws Exception;
    UserOuterClass.User getUser(String email) throws Exception;

    List<UserOuterClass.User> getUsers() throws Exception;
}
