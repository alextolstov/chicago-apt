package com.chicago.ext.components;

import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.common.util.ResponseFactoryUtil;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.ext.bll.UserBll;
import com.chicago.ext.dal.cassandra.PasswordNotMatchException;
import com.chicago.ext.dal.cassandra.UserNotFoundException;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UserRequests extends AbstractComponent
{
    private static final Logger _LOG = LoggerFactory.getLogger(UserRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private UserBll _userBll;

    private AbstractEventDispatcher _ed;

    public UserRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.CreateAdminUserRequest.class, new CreateAdminUserEventHandler());
        _ed.registerHandler(Usermessages.CreateUserRequest.class, new CreateUserEventHandler());
        _ed.registerHandler(Usermessages.LoginUserRequest.class, new LoginUserEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _userBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(UserBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class CreateAdminUserEventHandler implements EventHandler<Usermessages.CreateAdminUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.CreateAdminUserRequest event, String transactionId)
        {
            Message createUserResponse;
            try
            {
                UserOuterClass.User newUser = _userBll.createAdminUser(event.getUser());
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .setUser(newUser)
                        .build();
            } catch (Exception ex)
            {
                createUserResponse = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.CreateUserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), createUserResponse, transactionId));
            _LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class CreateUserEventHandler implements EventHandler<Usermessages.CreateUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.CreateUserRequest event, String transactionId)
        {
            Message createUserResponse;
            try
            {
                UserOuterClass.User newUser = _userBll.createStandardUser(event.getUser());
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .setUser(newUser)
                        .build();
            } catch (Exception ex)
            {
                createUserResponse = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.CreateUserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), createUserResponse, transactionId));
            _LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class LoginUserEventHandler implements EventHandler<Usermessages.LoginUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.LoginUserRequest event, String transactionId)
        {
            Message loginUserResponse;
            try
            {
                _userBll.authUser(event.getUser().getEmail(), event.getUser().getPassword());
                loginUserResponse = Usermessages.LoginUserResponse
                        .newBuilder()
                        .build();
            } catch (UserNotFoundException ex)
            {
                loginUserResponse = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_NOT_FOUND,
                        Usermessages.LoginUserResponse.class);
            } catch (PasswordNotMatchException ex)
            {
                loginUserResponse = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_UNAUTHORIZED,
                        Usermessages.LoginUserResponse.class);
            } catch (Exception ex)
            {
                loginUserResponse = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.LoginUserResponse.class);
            }

            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), loginUserResponse, transactionId));
            _LOG.info("Published real-time response with transaction id: {} and message type: {}", transactionId, loginUserResponse.getClass());
        }
    }
}
