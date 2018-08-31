import com.chicago.dto.Organization;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import com.chicago.services.internal.*;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class ProtobufTest extends JerseyTest
{
    @Override
    protected Application configure()
    {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(ProtobufResource.class);
    }

    @Path("/")
    public static class ProtobufResource
    {
        @GET
        @Produces(MediaTypeExt.APPLICATION_PROTOBUF)
        public Organization.Company getCompany()
        {
            return buildCompany();
        }
    }

    @Test
    public void serializePerson()
    {
//        CompanyOuterClass.Company company = target("/").request(MediaTypeExt.APPLICATION_PROTOBUF)
//                .get(CompanyOuterClass.Company.class);
//        assertEquals(1, company.getId());
//        assertEquals("John Doe", company.getName());
//        assertEquals("john.doe@google.com", company.getEmail());
    }

    private static Organization.Company buildCompany()
    {
        return Organization.Company.newBuilder()
                .build();
    }
}
