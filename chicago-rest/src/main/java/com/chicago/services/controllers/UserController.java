package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeoutException;

@Path("/users")
public class UserController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("avatar")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response setAvatar(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.hasRole("");
        try
        {
            UserOuterClass.UserAvatar userAvatar = UserOuterClass.UserAvatar.newBuilder()
                    .setAvatar(ByteString.copyFrom(data))
                    .build();
            byte[] response;

            Usermessages.SetUserAvatarRequest request = Usermessages.SetUserAvatarRequest.newBuilder()
                    .setUserAvatar(userAvatar)
                    .build();

            response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    @POST
    @Path("user")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getUser(byte[] data)
    {
        Subject currentUser = SecurityUtils.getSubject();
        return userOperations(data, Common.CrudOperation.READ);
    }

    @POST
    @Path("saveuser")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response saveUser(byte[] data)
    {
        return userOperations(data, Common.CrudOperation.UPDATE);
    }

    @POST
    @Path("createadmin")
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createAdminUser(byte[] data)
    {
        return createUser(data, true);
    }

    @POST
    @Path("create")
    @RequiresAuthentication
    @RequiresPermissions("user:create")
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createStandardUser(byte[] data)
    {
        return createUser(data, false);
    }

    private Response createUser(byte[] data, boolean isAdmin)
    {
        try
        {
            UserOuterClass.User user = UserOuterClass.User.parseFrom(data);
            byte[] response;

            Usermessages.UserRequest request = Usermessages.UserRequest.newBuilder()
                    .setCrudOperation(Common.CrudOperation.CREATE)
                    .setUserType(isAdmin ? Usermessages.UserType.ADMIN : Usermessages.UserType.STANDARD)
                    .setUser(user)
                    .build();

            response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private Response userOperations(byte[] data, Common.CrudOperation operation) {
        try
        {
            UserOuterClass.User user = UserOuterClass.User.parseFrom(data);
            byte[] response;

            Usermessages.UserRequest request = Usermessages.UserRequest.newBuilder()
                    .setCrudOperation(operation)
                    .setUser(user)
                    .build();

            response = _asyncComm.transaction(request);
            return Response.ok(response).build();
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
}
