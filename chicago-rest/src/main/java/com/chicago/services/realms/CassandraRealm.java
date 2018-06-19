package com.chicago.services.realms;

import com.chicago.common.comm.AsyncCommunicator;
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
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.inject.Inject;
import java.util.concurrent.TimeoutException;

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

        try
        {
            UserOuterClass.User user = UserOuterClass.User
                    .newBuilder()
                    .setEmail(username)
                    .setPassword(password)
                    .build();
            Usermessages.LoginUserRequest loginUserRequest = Usermessages.LoginUserRequest
                    .newBuilder()
                    .setUser(user)
                    .build();
            AsyncCommunicator asyncComm = Application.getServiceLocator().getInstance(AsyncCommunicator.class);
            byte[] response = asyncComm.transaction(loginUserRequest);
            Usermessages.LoginUserResponse loginUserResponse = Usermessages.LoginUserResponse.parseFrom(response);
            if (loginUserResponse.hasTransactionError())
            {
                throw new AuthenticationException(loginUserResponse.getTransactionError().getErrorMessage());
            }
        } catch (TimeoutException | InvalidProtocolBufferException e)
        {
            throw new AuthenticationException(e.getMessage());
        }

        return new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        return null;
    }
}
