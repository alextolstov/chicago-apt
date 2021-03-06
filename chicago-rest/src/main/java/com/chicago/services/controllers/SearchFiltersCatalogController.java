package com.chicago.services.controllers;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Searchfilters;
import com.chicago.dto.Searchfiltersmessages;
import com.chicago.dto.Service;
import com.chicago.dto.UserOuterClass;
import com.chicago.services.internal.MediaTypeExt;
import com.chicago.services.util.ResponseErrorUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/searchfilters")
public class SearchFiltersCatalogController
{
    @Inject
    Service.RestServiceConfig _config;

    @Inject
    private AsyncCommunicator _asyncComm;

    @POST
    @Path("get")
//    @RequiresAuthentication
    @Produces(MediaTypeExt.APPLICATION_OCTET_STREAM)
    public Response getSearchFiltersCatalog(byte[] data)
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

    private Searchfiltersmessages.SearchFiltersCatalogRequest prepareRequest(byte[] data, Common.CrudOperation operation) throws Exception
    {
        UserOuterClass.UserId userId = UserOuterClass.UserId.parseFrom(data);

        return Searchfiltersmessages.SearchFiltersCatalogRequest.newBuilder()
                .setCrudOperation(operation)
                .setUserId(userId.getUserId())
                .build();
    }
}
