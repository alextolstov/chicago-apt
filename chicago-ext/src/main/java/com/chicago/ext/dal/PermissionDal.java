package com.chicago.ext.dal;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;

import java.util.List;
import java.util.Set;

public interface PermissionDal
{
    Set<String> setSystemAdminRole(String userId) throws Exception;

    void setUserPermissions(String userId, List<PermissionOuterClass.Role> roles, List<PermissionOuterClass.Permission> extraPermissions) throws Exception;

    UserOuterClass.UserPermissions getUserPermissions(String userId);

    PermissionOuterClass.Roles getSystemPermissions();
}
