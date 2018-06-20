package com.chicago.services.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/login")
public class AuthController
{
    /**
     * SPA application helper. SPA works differently and need to know to show login page or not
     * If cookies dont match jump to login page will be shown
     *
     * @return 200 or 401
     */
    @GET
    @Path("testauth")
    public Response getUser()
    {
        Session session = SecurityUtils.getSubject().getSession(false);
        return (session != null) ? Response.ok(null).build() : Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
