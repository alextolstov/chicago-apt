package com.chicago.services.realms;

import com.chicago.common.comm.AsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.services.Application;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.DomainPermission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
//https://github.com/stormpath/stormpath-shiro/blob/master/core/src/main/java/com/stormpath/shiro/realm/ApplicationRealm.java

public class CassandraRealm extends AuthorizingRealm
{
    private PasswordService passwordService;

    @Override
    public String getName()
    {
        return "CassandraRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token)
    {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        String username = (String) token.getPrincipal();
        String password = new String(((UsernamePasswordToken) token).getPassword());
        PrincipalCollection principals;

        try
        {
            UserOuterClass.User.Builder userBuilder = UserOuterClass.User.newBuilder()
                    .setPassword(password);
            if (username.contains("@"))
            {
                userBuilder.setEmail(username);
            } else
            {
                userBuilder.setCellPhone(username);
            }

            Usermessages.LoginUserRequest loginUserRequest = Usermessages.LoginUserRequest
                    .newBuilder()
                    .setUser(userBuilder.build())
                    .build();
            AsyncCommunicator asyncComm = Application.getServiceLocator().getService(AsyncCommunicator.class);
            byte[] response = asyncComm.transaction(loginUserRequest);
            Usermessages.UserResponse loginUserResponse = Usermessages.UserResponse.parseFrom(response);
            if (loginUserResponse.hasTransactionError())
            {
                throw new AuthenticationException(loginUserResponse.getTransactionError().getErrorMessage());
            }

            // Principals are AUX info that we will keep in session
            principals = createPrincipals(loginUserResponse.getUser());

        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            throw new AuthenticationException(e.getMessage());
        }

        return new SimpleAuthenticationInfo(principals, password.toCharArray(), getName());
    }


    private PrincipalCollection createPrincipals(UserOuterClass.User user)
    {
        LinkedHashMap<String, String> props = new LinkedHashMap<>();
        props.put("username", user.getEmail());
        props.put("phone", user.getCellPhone());
        props.put("firstname", user.getFirstName());
        props.put("lastname", user.getLastName());
        props.put("user_id", user.getUserId());
        Collection<Object> principals = new ArrayList<>(2);
        principals.add(props);
        principals.add(user.getPermissionNamesList());
        return new SimplePrincipalCollection(props, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> permissionsList = (List<String>)principals.asList().get(1);
        info.addStringPermissions(permissionsList);
        return info;
    }
}
