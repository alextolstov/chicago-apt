package com.chicago.services.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.LinkedHashMap;

public class SecurityUtil
{
    public static String getSessionUserId()
    {
        LinkedHashMap<String, String> props = getSessionProps();
        return props.get("user_id");
    }

    private static LinkedHashMap<String, String> getSessionProps()
    {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection pcoll = (SimplePrincipalCollection)principals.getPrimaryPrincipal();
        return (LinkedHashMap<String, String>) pcoll.iterator().next();
    }
}
