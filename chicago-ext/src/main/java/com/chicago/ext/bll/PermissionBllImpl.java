package com.chicago.ext.bll;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.PermissionDal;
import org.jvnet.hk2.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Contract
public class PermissionBllImpl implements PermissionBll
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionBllImpl.class);
    @Inject
    private PermissionDal _permissionDal;

    @Override
    public void setUserPermissions(UserOuterClass.UserPermissions userPermissions) throws Exception
    {
        _permissionDal.setUserPermissions(userPermissions.getUserId(), userPermissions.getRolesList(), userPermissions.getExtraPermissionsList());
    }

    @Override
    public UserOuterClass.UserPermissions getUserPermissions(String userId)
    {
        return _permissionDal.getUserPermissions(userId);
    }

    @Override
    public PermissionOuterClass.Roles getSystemPermissions()
    {
        return _permissionDal.getSystemPermissions();
    }
}
