package com.chicago.app.test;

import com.chicago.app.ApplicationBinder;
import com.chicago.dto.*;
import com.chicago.ext.bll.InventoryBll;
import com.chicago.ext.bll.OrganizationBll;
import com.chicago.ext.bll.UserBll;
import com.chicago.ext.dal.PermissionDal;
import com.chicago.ext.dal.PositionDal;
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
    public void testSetCompany()
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

        // Locations
        InventoryOuterClass.InventoryLocation location = InventoryOuterClass.InventoryLocation.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setLocationName("Shelf")
                .build();
        InventoryOuterClass.InventoryLocation createdLocation = inventory.createInventoryLocation(location);

        InventoryOuterClass.InventoryLocation updatedLocation = InventoryOuterClass.InventoryLocation.newBuilder(createdLocation)
                .setLocationName("Closet")
                .build();
        inventory.updateInventoryLocation(updatedLocation);
        List<InventoryOuterClass.InventoryLocation> locations = inventory.getInventoryLocations("9df37802-95a9-425e-be0f-00f45b2a6c4a");

        // Brands
        InventoryOuterClass.InventoryItemBrand brand = InventoryOuterClass.InventoryItemBrand.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setBrandName("Nivea")
                .build();
        InventoryOuterClass.InventoryItemBrand createdBrand = inventory.createItemBrand(brand);

        InventoryOuterClass.InventoryItemBrand updatedBrand = InventoryOuterClass.InventoryItemBrand.newBuilder(createdBrand)
                .setBrandName("Gillet")
                .build();
        inventory.updateItemBrand(updatedBrand);
        List<InventoryOuterClass.InventoryItemBrand> brands = inventory.getItemBrands("9df37802-95a9-425e-be0f-00f45b2a6c4a");

        // Category
        InventoryOuterClass.InventoryItemCategory category = InventoryOuterClass.InventoryItemCategory.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setCategoryName("Shampoo")
                .build();
        InventoryOuterClass.InventoryItemCategory createdCategory = inventory.createItemCategory(category);

        category = InventoryOuterClass.InventoryItemCategory.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setCategoryName("Paint")
                .build();
        createdCategory = inventory.createItemCategory(category);

        InventoryOuterClass.InventoryItemCategory updatedCategory = InventoryOuterClass.InventoryItemCategory.newBuilder(createdCategory)
                .setCategoryName("Soap")
                .build();
        inventory.updateItemCategory(updatedCategory);
        List<InventoryOuterClass.InventoryItemCategory> categories = inventory.getItemCategories("9df37802-95a9-425e-be0f-00f45b2a6c4a");

        // Units
        InventoryOuterClass.InventoryItemUnit unit = InventoryOuterClass.InventoryItemUnit.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setUnitName("gr")
                .build();
        InventoryOuterClass.InventoryItemUnit createdUnit = inventory.createItemUnit(unit);

        InventoryOuterClass.InventoryItemSupplier supplier = InventoryOuterClass.InventoryItemSupplier.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setSupplierName("OOO Romashka")
                .build();
        InventoryOuterClass.InventoryItemSupplier createdSupplier = inventory.createItemSupplier(supplier);

        // Item
        InventoryOuterClass.InventoryCatalogItem item = InventoryOuterClass.InventoryCatalogItem.newBuilder()
                .setEntityId("9df37802-95a9-425e-be0f-00f45b2a6c4a")
                .setItemCategoryId(createdCategory.getCategoryId())
                .setItemBrandId(createdBrand.getBrandId())
                .setItemUnitId(createdUnit.getUnitId())
                .setItemSupplierId(createdSupplier.getSupplierId())
                .setLocationId(createdLocation.getLocationId())
                .setDescription("Strange thing")
                .setInboundUnitId(createdUnit.getUnitId())
                .setOutboundUnitId(createdUnit.getUnitId())
                .build();
        InventoryOuterClass.InventoryCatalogItem newItem = inventory.createInventoryCatalogItem(item);
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
            List<PermissionOuterClass.Role> roles = pd.getSystemRoles();

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
            UserOuterClass.UserPermissions userPerm = pd.getUserPermissions("a5a3e208-c10f-4eb2-b7ad-04d4bbbb13de");
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
