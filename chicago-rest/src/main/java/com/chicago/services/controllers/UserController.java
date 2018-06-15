package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.services.internal.MediaTypeExt;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.glassfish.jersey.server.ManagedAsync;

import javax.inject.Inject;
import javax.ws.rs.GET;
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

//    @Inject
//    Config.ZooKeeperConfig zooConfig;
//    @Inject
//    Config.KafkaConfig kafkaConfig;
    @Inject
    AsyncCommunicator asyncComm;

    @GET
    @Produces(MediaTypeExt.APPLICATION_JSON)
    public Response auth() throws TimeoutException, InvalidProtocolBufferException
    {
        return Response.ok(null).build();
    }

    @POST
    @Path("create")
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response createUser(byte[] data)
    {
        try
        {
            UserOuterClass.User usr = UserOuterClass.User.parseFrom(data);
            Usermessages.CreateUserRequest createRequest = Usermessages.CreateUserRequest.newBuilder()
                    .setUser(usr)
                    .build();
            byte[] response = asyncComm.transaction(createRequest);
            return Response.ok(response).build();
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            Common.TransactionError transactionError = Common.TransactionError.newBuilder()
                    .setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .setErrorMessage(e.getMessage())
                    .build();
            String jsonError = null;
            try
            {
                jsonError = JsonFormat.printer().print(transactionError);
            } catch (InvalidProtocolBufferException e1)
            {
                e1.printStackTrace();
            }
            return Response.serverError().entity(jsonError).type("application/json").build();
        }
    }
}
