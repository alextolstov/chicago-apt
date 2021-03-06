package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.CityOuterClass;
import com.chicago.dto.Citymessages;
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

@Path("/city")
public class CityController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("get")
//    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getCity(byte[] data)
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

    private Citymessages.CityRequest prepareRequest(byte[] data, Common.CrudOperation operation) throws Exception
    {
        CityOuterClass.City city = CityOuterClass.City.parseFrom(data);

        return Citymessages.CityRequest.newBuilder()
                .setCrudOperation(operation)
                .setCity(city)
                .build();
    }
}
