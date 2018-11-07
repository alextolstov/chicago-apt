package com.chicago.ext;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class TestUserDal
{
    AbstractBinder setBinder()
    {
        return new AbstractBinder()
        {
            @Override
            protected void configure()
            {

            }
        };
    }

    void setup()
    {
        AbstractBinder binder = setBinder();
        ServiceLocator locator = ServiceLocatorUtilities.bind("test", binder);
//        locator.getService();
    }

}
