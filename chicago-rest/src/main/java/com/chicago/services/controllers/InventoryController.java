package com.chicago.services.controllers;

import com.chicago.dto.Service;
import com.chicago.services.internal.MediaTypeExt;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.xml.ws.Response;

@Path("/inventory")
public class InventoryController
{
    @Inject
    Service.RestServiceConfig _config;

    @POST
    @Path("brand/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createBrand(byte[] data)
    {

    }

    @POST
    @Path("brand/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateBrand(byte[] data)
    {

    }

    @POST
    @Path("brand/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getBrands()
    {

    }

    @POST
    @Path("category/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createCategory(byte[] data)
    {

    }

    @POST
    @Path("category/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateCategory(byte[] data)
    {

    }

    @POST
    @Path("category/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getCategories()
    {

    }

    @POST
    @Path("unit/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createUnit(byte[] data)
    {

    }

    @POST
    @Path("unit/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateUnit(byte[] data)
    {

    }

    @POST
    @Path("category/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getUnits()
    {

    }

    @POST
    @Path("supplier/create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createSupplier(byte[] data)
    {

    }

    @POST
    @Path("supplier/update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateSupplier(byte[] data)
    {

    }

    @POST
    @Path("supplier/getall")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getSuppliers()
    {

    }

}
