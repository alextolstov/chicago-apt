package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.dto.Organizationmessages;
import com.chicago.dto.Service;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import com.chicago.services.util.SecurityUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/organization")
public class OrganizationController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createOrganization(byte[] data) throws InvalidProtocolBufferException
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        return executeRequest(OrganizationOuterClass.Organization.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updateOrganization(byte[] data) throws InvalidProtocolBufferException
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        return executeRequest(OrganizationOuterClass.Organization.parseFrom(data), Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("get")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getOrganization(byte[] data) throws InvalidProtocolBufferException
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        return executeRequest(OrganizationOuterClass.Organization.parseFrom(data), Common.CrudOperation.READ);
    }

    @POST
    @Path("getstructure")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getStructure(byte[] data)
    {
        String userId = SecurityUtil.getSessionUserId();
//        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        try
        {
            Organizationmessages.OrganizationStructureRequest request = Organizationmessages.OrganizationStructureRequest.newBuilder()
                    .setUserId(userId)
                    .setOrganizationType(OrganizationOuterClass.OrganizationType.HOLDING)
                    .build();

            byte[] response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response executeRequest(OrganizationOuterClass.Organization organization, Common.CrudOperation operation)
    {
        try
        {
            Organizationmessages.OrganizationRequest request = Organizationmessages.OrganizationRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setOrganization(organization)
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
