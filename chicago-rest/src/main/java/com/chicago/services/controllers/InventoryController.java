package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.InventoryOuterClass;
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
        return executeRequest(InventoryOuterClass.InventoryItemBrand.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("brand/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateBrand(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemBrand.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("brand/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getBrands()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryItemBrand brand = InventoryOuterClass.InventoryItemBrand.newBuilder()
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
        return executeRequest(InventoryOuterClass.InventoryItemCategory.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("category/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateCategory(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemCategory.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("category/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getCategories()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryItemCategory category = InventoryOuterClass.InventoryItemCategory.newBuilder()
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
        return executeRequest(InventoryOuterClass.InventoryItemUnit.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("unit/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateUnit(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemUnit.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("unit/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getUnits() throws InvalidProtocolBufferException
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryItemUnit unit = InventoryOuterClass.InventoryItemUnit.newBuilder()
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
        return executeRequest(InventoryOuterClass.InventoryItemSupplier.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("supplier/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateSupplier(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemSupplier.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("supplier/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getSuppliers()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryItemSupplier supplier = InventoryOuterClass.InventoryItemSupplier.newBuilder()
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
        return executeRequest(InventoryOuterClass.InventoryLocation.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("location/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateLocation(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryLocation.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("location/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getLocations()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryLocation location = InventoryOuterClass.InventoryLocation.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(location, Common.CrudOperation.READ);
    }

    // Inventory Item
    @POST
    @Path("item/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createItem(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemBrand.parseFrom(data), Common.CrudOperation.CREATE);
    }

    @POST
    @Path("brand/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateItem(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemBrand.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("brand/get")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getItem(byte[] data) throws InvalidProtocolBufferException
    {
        return executeRequest(InventoryOuterClass.InventoryItemBrand.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("brand/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getItems()
    {
        String entityId = SecurityUtil.getSessionEntityId();
        InventoryOuterClass.InventoryItem item = InventoryOuterClass.InventoryItem.newBuilder()
                .setEntityId(entityId)
                .build();
        return executeRequest(item, Common.CrudOperation.READ);
    }

    private Response executeRequest(InventoryOuterClass.InventoryItem item, Common.CrudOperation operation)
    {
        try
        {
            Inventorymessages.InventoryItemRequest request = Inventorymessages.InventoryItemRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setInventoryItem(item)
                    .build();
            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(InventoryOuterClass.InventoryItemBrand brand, Common.CrudOperation operation)
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

    private Response executeRequest(InventoryOuterClass.InventoryItemCategory category, Common.CrudOperation operation)
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

    private Response executeRequest(InventoryOuterClass.InventoryItemUnit unit, Common.CrudOperation operation)
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

    private Response executeRequest(InventoryOuterClass.InventoryItemSupplier supplier, Common.CrudOperation operation)
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

    private Response executeRequest(InventoryOuterClass.InventoryLocation location, Common.CrudOperation operation)
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
