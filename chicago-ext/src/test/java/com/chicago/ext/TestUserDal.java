package com.chicago.ext;

import com.datastax.driver.core.ResultSet;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

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

    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(
            new ClassPathCQLDataSet("cassandra_schema.cql", true, true));

    @Test
    public void should_have_started_and_execute_cql_script() 
    {
        cassandraCQLUnit.session.execute("insert into ChicagoErp.Organizations (organization_id) values(6ab09bec-e68e-48d9-a5f8-97e6fb4c9b47)");
        ResultSet result = cassandraCQLUnit.session.execute("select * from ChicagoErp.Organizations");
        UUID s = result.iterator().next().getUUID("organization_id");
    }

}
