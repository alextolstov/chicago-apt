package com.chicago.services.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;

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
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        if (principals == null)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        SimplePrincipalCollection pcoll = (SimplePrincipalCollection)principals.getPrimaryPrincipal();
        LinkedHashMap<String, String> props = (LinkedHashMap<String, String>) pcoll.iterator().next();
        String user_id = props.get("user_id");
        return Response.ok(user_id).build();
    }
}
