package com.chicago.common.dal;

import com.chicago.dto.UserOuterClass;

import java.util.List;

public interface UserDal
{
    List<UserOuterClass.User> getUsers();
}
