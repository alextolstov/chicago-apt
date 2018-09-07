package com.chicago.ext.components;

import com.chicago.common.core.*;
import com.chicago.common.util.ResponseFactoryUtil;
import com.chicago.dto.Common;
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
    private static final Logger LOG = LoggerFactory.getLogger(UserRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private UserBll _userBll;

    private AbstractEventDispatcher _ed;

    public UserRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.SetUserAvatarRequest.class, new SetUserAvatarHandler());
        _ed.registerHandler(Usermessages.SetUserPasswordRequest.class, new SetUserPasswordHandler());
        _ed.registerHandler(Usermessages.UserRequest.class, new UserEventHandler());
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

    class SetUserPasswordHandler implements EventHandler<Usermessages.SetUserPasswordRequest>
    {
        @Override
        public void handleEvent(Usermessages.SetUserPasswordRequest event, String transactionId)
        {
            Message response;
            try
            {
                _userBll.setUserPassword(event.getUserPassword());

                response = Common.VoidResponse
                        .newBuilder()
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class SetUserAvatarHandler implements EventHandler<Usermessages.SetUserAvatarRequest>
    {
        @Override
        public void handleEvent(Usermessages.SetUserAvatarRequest event, String transactionId)
        {
            Message response;
            try
            {
                _userBll.setUserAvatar(event.getUserAvatar());

                response = Common.VoidResponse
                        .newBuilder()
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class UserEventHandler implements EventHandler<Usermessages.UserRequest>
    {
        @Override
        public void handleEvent(Usermessages.UserRequest event, String transactionId)
        {
            Message response = null;
            try
            {
                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        UserOuterClass.User newUser = null;
                        if (event.getUserType() == Usermessages.UserType.ADMIN)
                        {
                            newUser = _userBll.createAdminUser(event.getUser());
                        } else
                        {
                            newUser = _userBll.createStandardUser(event.getUser());
                        }
                        response = Usermessages.UserResponse
                                .newBuilder()
                                .setUser(newUser)
                                .build();
                    }
                }
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class LoginUserEventHandler implements EventHandler<Usermessages.LoginUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.LoginUserRequest event, String transactionId)
        {
            Message response;
            try
            {
                _userBll.authUser(event.getUser().getEmail(), event.getUser().getPassword());
                response = Common.VoidResponse
                        .newBuilder()
                        .build();
            } catch (UserNotFoundException ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_NOT_FOUND,
                        Common.VoidResponse.class);
            } catch (PasswordNotMatchException ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_UNAUTHORIZED,
                        Common.VoidResponse.class);
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Common.VoidResponse.class);
            }

            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response with transaction id: {} and message type: {}", transactionId, response.getClass());
        }
    }
}
