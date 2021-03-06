package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Permissionmessages;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;

@Path("/permissions")
public class PermissionController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("update")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response updatePermissions(byte[] data)
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
    @Path("get")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getPermissions(byte[] data)
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

    @POST
    @Path("getsystemroles")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getSystemRoles()
    {
        Subject currentUser = SecurityUtils.getSubject();
        //currentUser.hasRole("");
        try
        {
            byte[] response = _asyncComm.transaction(prepareRequest());
            return Response.ok(response).build();
        } catch (Exception e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Permissionmessages.UserPermissionsRequest prepareRequest(byte[] data, Common.CrudOperation operation) throws Exception
    {
        UserOuterClass.UserPermissions permissions = UserOuterClass.UserPermissions.parseFrom(data);

        return Permissionmessages.UserPermissionsRequest.newBuilder()
                .setCrudOperation(operation)
                .setPermissions(permissions)
                .build();
    }

    private Permissionmessages.SystemRolesRequest prepareRequest()
    {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection pcoll = (SimplePrincipalCollection)principals.getPrimaryPrincipal();
        LinkedHashMap<String, String> props = (LinkedHashMap<String, String>) pcoll.iterator().next();
        String user_id = props.get("user_id");

        return Permissionmessages.SystemRolesRequest.newBuilder()
                .setUserId(user_id)
                .build();
    }
}
