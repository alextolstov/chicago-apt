package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.*;
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

@Path("/position")
public class PositionController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("create")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createPosition(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        try
        {
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
    public Response updatePosition(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
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

    @POST
    @Path("delete")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response deletePosition(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        try
        {
            byte[] response = _asyncComm.transaction(prepareRequest(data, Common.CrudOperation.DELETE));
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    @POST
    @Path("get")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getPositions(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        try
        {
            byte[] response = _asyncComm.transaction(prepareRequest(data, Common.CrudOperation.READ));
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Positionmessages.PositionRequest prepareRequest(byte[] data, Common.CrudOperation operation) throws Exception
    {
        PositionOuterClass.Position position = PositionOuterClass.Position.parseFrom(data);
        byte[] response;

        return Positionmessages.PositionRequest.newBuilder()
                .setCrudOperation(operation)
                .setPosition(position)
                .build();
    }

    private Positionmessages.PositionsRequest prepareRequest(byte[] data) throws Exception
    {
        PositionOuterClass.Positions positions = PositionOuterClass.Positions.parseFrom(data);
        byte[] response;

        return Positionmessages.PositionsRequest.newBuilder()
                .setOrganizationId(positions.getOrganizationId())
                .build();
    }
}
