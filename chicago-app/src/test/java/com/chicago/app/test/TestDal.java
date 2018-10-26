package com.chicago.app.test;

import com.chicago.app.ApplicationBinder;
import com.chicago.dto.PositionOuterClass;
import com.chicago.ext.dal.PositionDal;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestDal
{
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
    @DisplayName("")
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
            PositionOuterClass.Positions positions = pd.getPositions("2a328392-28b3-4f8e-a86b-a48d72819ef5");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
