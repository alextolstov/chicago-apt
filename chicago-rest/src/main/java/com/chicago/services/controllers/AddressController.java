package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.AddressOuterClass;
import com.chicago.dto.Addressmessages;
import com.chicago.dto.Common;
import com.chicago.dto.Service;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/address")
public class AddressController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createAddress(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.hasRole("");
        try
        {
            AddressOuterClass.Address address = AddressOuterClass.Address.parseFrom(data);
            byte[] response = _asyncComm.transaction(prepareRequest(data, Common.CrudOperation.CREATE));
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    @POST
    @Path("update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateAddress(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.hasRole("");
        try
        {
            byte[] response = _asyncComm.transaction(prepareRequest(data, Common.CrudOperation.UPDATE));
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Addressmessages.AddressRequest prepareRequest(byte[] data, Common.CrudOperation operation) throws Exception
    {
        AddressOuterClass.Address address = AddressOuterClass.Address.parseFrom(data);
        byte[] response;

        return Addressmessages.AddressRequest.newBuilder()
                .setCrudOperation(operation)
                .setAddress(address)
                .build();
    }
}
