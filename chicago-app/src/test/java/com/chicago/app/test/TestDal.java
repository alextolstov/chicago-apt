package com.chicago.app.test;

import com.chicago.app.ApplicationBinder;
import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.bll.InventoryBll;
import com.chicago.ext.bll.OrganizationBll;
import com.chicago.ext.bll.UserBll;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.PermissionDal;
import com.chicago.ext.dal.PositionDal;
import com.chicago.ext.dal.UserDal;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestDal
{
    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(
            new ClassPathCQLDataSet("cassandra_schema.cql", true, true));

    @Test
    public void setCompany()
    {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
        TestCassandraConnector ts = new TestCassandraConnector(cassandraCQLUnit);
        ServiceLocatorUtilities.bind(serviceLocator, new TestApplicationBinder(ts));
        UserBll userbll = serviceLocator.getService(UserBll.class);
        UserOuterClass.User newuser = UserOuterClass.User.newBuilder()
                .setFirstName("Aleksey")
                .setLastName("Telyshev")
                .setEmail("atelyshev@gmail.com")
                .setPassword("12345678")
                .setCellPhone("+19089061234")
                .build();
        try
        {
            newuser = userbll.createAdminUser(newuser);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        OrganizationBll organizationbll = serviceLocator.getService(OrganizationBll.class);
        try
        {
            OrganizationOuterClass.OrganizationInfo orgInfo = organizationbll.getOrganizationStructure(newuser.getUserId());
            OrganizationOuterClass.Organization org = OrganizationOuterClass.Organization.newBuilder()
                    .setParentOrganizationId(newuser.getOrganizationId())
                    .setName("Aleksey branch")
                    .build();
            org = organizationbll.createOrganization(org);

            org = OrganizationOuterClass.Organization.newBuilder()
                    .setParentOrganizationId(orgInfo.getOrganizationId())
                    .setName("Aleksey company")
                    .build();
            org = organizationbll.createOrganization(org);

            orgInfo = organizationbll.getOrganizationStructure(newuser.getUserId());
            System.out.print("!");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testInventoryItems()
    {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
        TestCassandraConnector ts = new TestCassandraConnector(cassandraCQLUnit);
        ServiceLocatorUtilities.bind(serviceLocator, new TestApplicationBinder(ts));
        InventoryBll inventory = serviceLocator.getService(InventoryBll.class);

        Inventory.InventoryItemBrand brand = Inventory.InventoryItemBrand.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setBrandName("Nivea")
                .build();
        Inventory.InventoryItemBrand createdBrand = inventory.createItemBrand(brand);

        Inventory.InventoryItemBrand updatedBrand = Inventory.InventoryItemBrand.newBuilder(createdBrand)
                .setBrandName("Gillet")
                .build();
        inventory.updateItemBrand(updatedBrand);
        List<Inventory.InventoryItemBrand> brands = inventory.getItemBrands("9df37802-95a9-425e-be0f-00f45b2a6c4a");

        Inventory.InventoryItemCategory category = Inventory.InventoryItemCategory.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setCategoryName("Shampoo")
                .build();
        Inventory.InventoryItemCategory createdCategory = inventory.createItemCategory(category);

        category = Inventory.InventoryItemCategory.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setCategoryName("Paint")
                .build();
        createdCategory = inventory.createItemCategory(category);

        Inventory.InventoryItemCategory updatedCategory = Inventory.InventoryItemCategory.newBuilder(createdCategory)
                .setCategoryName("Soap")
                .build();
        inventory.updateItemCategory(updatedCategory);
        List<Inventory.InventoryItemCategory> categories = inventory.getItemCategories("9df37802-95a9-425e-be0f-00f45b2a6c4a");
    }

    @Test
    public void getPermissions()
    {
        try
        {
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            TestCassandraConnector ts = new TestCassandraConnector(cassandraCQLUnit);
            ServiceLocatorUtilities.bind(serviceLocator, new TestApplicationBinder(ts));
            UserBll ub = serviceLocator.getService(UserBll.class);
            UserOuterClass.User user = UserOuterClass.User.newBuilder()
                    .setEmail("test@gmail.com")
                    .setFirstName("John")
                    .setLastName("Smith")
                    .build();
            UserOuterClass.User newuser = ub.createAdminUser(user);
            OrganizationBll ob = serviceLocator.getService(OrganizationBll.class);
            OrganizationOuterClass.OrganizationInfo oi = ob.getOrganizationStructure(newuser.getUserId());
            OrganizationOuterClass.OrganizationInfo oinfo = oi.getOrganizationsList().get(0);
            List<UserOuterClass.User> users = ub.getUsers(oinfo.getOrganizationId());
            OrganizationOuterClass.Organization org = OrganizationOuterClass.Organization.newBuilder()
                    .setOrganizationId(oinfo.getOrganizationId())
                    .build();
            org = ob.getOrganization(org);
            OrganizationOuterClass.Organization newOrg = OrganizationOuterClass.Organization.newBuilder()
                    .mergeFrom(org)
                    .setName("Test org")
                    .build();
            ob.updateOrganization(newOrg);
            users = ub.getUsers(newOrg.getOrganizationId());

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
