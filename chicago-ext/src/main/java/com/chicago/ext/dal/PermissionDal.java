package com.chicago.ext.dal;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;

public interface PermissionDal
{
    void setUserPermissions(UserOuterClass.UserPermissions permissions);

    UserOuterClass.UserPermissions getUserPermissions(String userId);

    PermissionOuterClass.Roles getSystemPermissions();
}
