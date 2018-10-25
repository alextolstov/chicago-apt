package com.chicago.ext.bll;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.UserOuterClass;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface PermissionBll
{
    void setUserPermissions(UserOuterClass.UserPermissions permissions);

    UserOuterClass.UserPermissions getUserPermissions(String userId);

    PermissionOuterClass.Roles getSystemPermissions();
}
