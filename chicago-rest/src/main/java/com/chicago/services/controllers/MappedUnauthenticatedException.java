package com.chicago.services.controllers;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MappedUnauthenticatedException extends UnauthenticatedException implements
        ExceptionMapper<UnauthenticatedException>
{
    private static final long serialVersionUID = 1L;

    public MappedUnauthenticatedException()
    {
        super("Unauthenticated");
    }

    public MappedUnauthenticatedException(String string)
    {
        super(string);
    }

    @Override
    public Response toResponse(UnauthenticatedException exception)
    {
        return Response.status(401).entity(exception.getMessage())
                .type("text/plain").build();
    }
}
