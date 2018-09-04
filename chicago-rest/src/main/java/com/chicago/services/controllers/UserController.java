package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
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
    @Path("image")
    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response setImage(byte[] data) throws TimeoutException, InvalidProtocolBufferException
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.hasRole("");
        return Response.ok(null).build();
    }

    @POST
    @Path("createadminuser")
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
            UserOuterClass.User usr = UserOuterClass.User.parseFrom(data);
            byte[] response;

            if (isAdmin)
            {
                Usermessages.CreateAdminUserRequest createRequest = Usermessages.CreateAdminUserRequest.newBuilder()
                        .setUser(usr)
                        .build();
                response = _asyncComm.transaction(createRequest);
            } else
            {
                Usermessages.CreateUserRequest createRequest = Usermessages.CreateUserRequest.newBuilder()
                        .setUser(usr)
                        .build();
                response = _asyncComm.transaction(createRequest);
            }

            return Response.ok(response).build();
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            return ResponseErrorUtil.createErrorResponse(e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
}
