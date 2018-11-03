package com.chicago.ext.dal;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;
import javafx.util.Pair;

import java.util.List;

public interface UserDal
{
    String createUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt)
            throws Exception;

    boolean isUserExists(String email);

    void updateUser(UserOuterClass.User user) throws Exception;

    void setUserPassword(String user_id, String passwordHash, byte[] passwordSalt) throws Exception;

    void setUserAvatar(UserOuterClass.UserAvatar avatar) throws Exception;

    void setUserAddress(String userId, String addressId) throws Exception;

    UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception;

    Pair<String, byte[]> getHashSalt(String email) throws Exception;

    UserOuterClass.User getUserByEmail(String email) throws Exception;

    UserOuterClass.User getUserById(String userId) throws Exception;

    List<UserOuterClass.User> getUsers(String organizationId) throws Exception;
}
