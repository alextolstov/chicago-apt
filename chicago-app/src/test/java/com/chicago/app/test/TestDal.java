package com.chicago.app.test;

import com.chicago.app.ApplicationBinder;
import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.PermissionDal;
import com.chicago.ext.dal.PositionDal;
import com.chicago.ext.dal.UserDal;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestDal
{
    @Test
    public void getPermissions()
    {
        try
        {
            String confFile = "file://d:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PermissionDal pd = serviceLocator.getService(PermissionDal.class);
            PermissionOuterClass.Roles roles = pd.getSystemPermissions();

            PermissionOuterClass.Role role = PermissionOuterClass.Role.newBuilder()
                    .setRoleId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                    .build();
            List<PermissionOuterClass.Permission> perm = new ArrayList<>();
            PermissionOuterClass.Permission permission1 = PermissionOuterClass.Permission.newBuilder()
                    .setPermissionId(1)
                    .build();
            perm.add(permission1);
            PermissionOuterClass.Permission permission2 = PermissionOuterClass.Permission.newBuilder()
                    .setPermissionId(5)
                    .build();
            perm.add(permission2);
            UserOuterClass.UserPermissions up = UserOuterClass.UserPermissions.newBuilder()
                    .setUserId("a5a3e208-c10f-4eb2-b7ad-04d4bbbb13de")
                    .addRoles(role)
                    .addAllExtraPermissions(perm)
                    .build();
            pd.setUserPermissions(up.getUserId(), up.getRolesList(), up.getExtraPermissionsList());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void createPos()
    {
        try
        {
            String confFile = "file://C:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PositionDal pd = serviceLocator.getService(PositionDal.class);
            PositionOuterClass.Position pos = PositionOuterClass.Position.newBuilder()
                    .setOrganizationId("bfbdf629-dada-4ecf-aacc-343d9aa609ba")
                    .setDescription("Barber1")
                    .build();
            PositionOuterClass.Position p = pd.createPosition(pos);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void updatePos()
    {
        try
        {
            String confFile = "file://C:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PositionDal pd = serviceLocator.getService(PositionDal.class);
            PositionOuterClass.Position pos = PositionOuterClass.Position.newBuilder()
                    .setOrganizationId(UUID.randomUUID().toString())
                    .setPositionId(UUID.randomUUID().toString())
                    .setDescription("BarberChanged")
                    .build();
            pd.updatePosition(pos);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFromMap()
    {
        try
        {
            String confFile = "file://C:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PositionDal pd = serviceLocator.getService(PositionDal.class);
            PositionOuterClass.Position pos = PositionOuterClass.Position.newBuilder()
                    .setOrganizationId("2a328392-28b3-4f8e-a86b-a48d72819ef5")
                    .setPositionId("6b6bd32a-db64-4bd4-b2c2-f39dca26e5c7")
                    .build();
            pd.deletePosition(pos);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void getPositions()
    {
        try
        {
            String confFile = "file://C:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PositionDal pd = serviceLocator.getService(PositionDal.class);
            Map<UUID, String> positions = pd.getPositions("2a328392-28b3-4f8e-a86b-a48d72819ef5");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
