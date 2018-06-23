package com.chicago.services.controllers;

import org.apache.shiro.authz.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MappedUnauthorizedException extends UnauthorizedException implements
        ExceptionMapper<UnauthorizedException>
{
    private static final long serialVersionUID = 1L;

    public MappedUnauthorizedException()
    {
        super("Unauthorized");
    }

    public MappedUnauthorizedException(String string)
    {
        super(string);
    }

    @Override
    public Response toResponse(UnauthorizedException exception)
    {
        return Response.status(403).entity(exception.getMessage())
                .type("text/plain").build();
    }
}
