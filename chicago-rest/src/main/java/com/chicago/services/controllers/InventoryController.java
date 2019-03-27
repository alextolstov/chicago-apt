package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Inventory;
import com.chicago.dto.Inventorymessages;
import com.chicago.dto.Service;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import com.chicago.services.util.SecurityUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/inventory")
public class InventoryController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("brand/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createBrand(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemBrand.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("brand/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateBrand(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemBrand.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("brand/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getBrands()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        Inventory.InventoryItemBrand brand = Inventory.InventoryItemBrand.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(brand, Common.CrudOperation.READ);
    }

    @POST
    @Path("category/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createCategory(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemCategory.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("category/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateCategory(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemCategory.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("category/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getCategories()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        Inventory.InventoryItemCategory category = Inventory.InventoryItemCategory.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(category, Common.CrudOperation.READ);
    }

    @POST
    @Path("unit/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createUnit(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemUnit.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("unit/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateUnit(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemUnit.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("unit/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getUnits() throws InvalidProtocolBufferException
    {
        String entityId = SecurityUtil.getSessionEntityId();
        Inventory.InventoryItemUnit unit = Inventory.InventoryItemUnit.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(unit, Common.CrudOperation.READ);
    }

    @POST
    @Path("supplier/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createSupplier(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemSupplier.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("supplier/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateSupplier(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryItemSupplier.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("supplier/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getSuppliers()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        Inventory.InventoryItemSupplier supplier = Inventory.InventoryItemSupplier.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(supplier, Common.CrudOperation.READ);
    }

    @POST
    @Path("location/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createLocation(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryLocation.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("location/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateLocation(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(Inventory.InventoryLocation.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("location/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getLocations()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        Inventory.InventoryLocation location = Inventory.InventoryLocation.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(location, Common.CrudOperation.READ);
    }

    private Response executeRequest(Inventory.InventoryItemBrand brand, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryItemBrandRequest request = Inventorymessages.InventoryItemBrandRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setItemBrand(brand)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(Inventory.InventoryItemCategory category, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryItemCategoryRequest request = Inventorymessages.InventoryItemCategoryRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setItemCategory(category)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(Inventory.InventoryItemUnit unit, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryItemUnitRequest request = Inventorymessages.InventoryItemUnitRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setItemUnit(unit)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(Inventory.InventoryItemSupplier supplier, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryItemSupplierRequest request = Inventorymessages.InventoryItemSupplierRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setItemSupplier(supplier)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(Inventory.InventoryLocation location, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryLocationRequest request = Inventorymessages.InventoryLocationRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setInventoryLocation(location)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
}
