package com.chicago.services.controllers;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.common.component.user.UserRequests;
import com.chicago.dto.Config;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.services.internal.MediaTypeExt;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.concurrent.TimeoutException;

@Path("/users")
public class UserController {
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    Config.ZooKeeperConfig zooConfig;
    @Inject
    Config.KafkaConfig kafkaConfig;
    @Inject
    AsyncCommunicator asyncComm;

    @GET
//    @Produces(MediaTypeExt.APPLICATION_PROTOBUF)
    public Response auth() throws TimeoutException, InvalidProtocolBufferException
    {
        return Response.ok(null).build();
    }

    @POST
    @Path("create")
    @Produces(MediaTypeExt.APPLICATION_PROTOBUF)
    public Response createUser() throws TimeoutException, InvalidProtocolBufferException
    {
        UserOuterClass.User usr = UserOuterClass.User.newBuilder()
                .setFirstName("Aleksey")
                .setLastName("Telyshev")
                .build();
        Usermessages.CreateUserRequest createReq = Usermessages.CreateUserRequest.newBuilder()
                .setUser(usr)
                .build();
        byte[] response = asyncComm.transaction(createReq);
        return Response.ok(null).build();
    }
}
