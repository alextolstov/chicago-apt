package com.chicago.app.test.integration;

import com.chicago.app.ApplicationBinder;
import com.chicago.app.ServiceEntry;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.Service;
import com.chicago.ext.dal.PositionDal;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;

import java.util.UUID;

public class TestDal
{
    @Test
    public void UpdateMap()
    {
        try
        {
            String confFile = "file://C:\\dev\\chicago-erp\\chicago-app\\src\\main\\resources\\config\\userservice.cfg";
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            PositionDal pd = serviceLocator.getService(PositionDal.class);
            PositionOuterClass.Position pos = PositionOuterClass.Position.newBuilder()
                    .setEntityId(UUID.randomUUID().toString())
                    .setDescription("Barber")
                    .build();
            PositionOuterClass.Position p = pd.createPosition(pos);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}