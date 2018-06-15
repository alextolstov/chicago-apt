package com.chicago.services.controllers;

import com.chicago.services.internal.MediaTypeExt;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/login")
public class AuthController
{
//    @Inject
//    AsyncCommunicator asyncComm;

    @POST
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getUser()
    {
        //asyncComm.transaction();
        return Response.ok(null).build();
    }
}
