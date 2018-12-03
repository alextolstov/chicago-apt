package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.*;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.chicago.dto.Common;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.chicago.ext.bll.UserBll;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class UserRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(UserRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private UserBll _userBll;

    public UserRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.SetUserAvatarRequest.class, new SetUserAvatarEventHandler());
        _ed.registerHandler(Usermessages.SetUserPasswordRequest.class, new SetUserPasswordEventHandler());
        _ed.registerHandler(Usermessages.UserRequest.class, new UserEventHandler());
        _ed.registerHandler(Usermessages.LoginUserRequest.class, new LoginUserEventHandler());
        _ed.registerHandler(Usermessages.GetUsersRequest.class, new GetUsersEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Usermessages.UserResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Usermessages.GetUsersResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _userBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(UserBll.class);
        LOG.info("Service locator found UserBll");
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    // In: Usermessages.SetUserPasswordRequest
    // Out: Common.VoidResponse
    class SetUserPasswordEventHandler implements EventHandler<Usermessages.SetUserPasswordRequest>
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
            }
            catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    // In: Usermessages.SetUserAvatarRequest
    // Out: Common.VoidResponse
    class SetUserAvatarEventHandler implements EventHandler<Usermessages.SetUserAvatarRequest>
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
            }
            catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    // In: Usermessages.UserRequest
    // Out: Usermessages.UserResponse
    class UserEventHandler implements EventHandler<Usermessages.UserRequest>
    {
        @Override
        public void handleEvent(Usermessages.UserRequest event, String transactionId)
        {
            Message response;
            try
            {
                UserOuterClass.User user = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        if (event.getUserType() == Usermessages.UserType.ADMIN)
                        {
                            user = _userBll.createAdminUser(event.getUser());
                        } else
                        {
                            user = _userBll.createStandardUser(event.getUser());
                        }
                        break;
                    }
                    case READ:
                    {
                        if (!event.getUser().getEmail().isEmpty())
                        {
                            user = _userBll.getUserByEmail(event.getUser().getEmail());
                        } else if (!event.getUser().getUserId().isEmpty())
                        {
                            user = _userBll.getUserById(event.getUser().getUserId());
                        } else
                        {
                            throw new Exception("email or userId should be specified");
                        }
                        break;
                    }
                    case UPDATE:
                    {
                        _userBll.updateUser(event.getUser());
                        user = UserOuterClass.User.getDefaultInstance();
                        break;
                    }
                }

                response = Usermessages.UserResponse
                        .newBuilder()
                        .setUser(user)
                        .build();
            }
            catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    // In: Usermessages.GetUsersRequest
    // Out: Usermessages.GetUserResponse
    class GetUsersEventHandler implements EventHandler<Usermessages.GetUsersRequest>
    {
        @Override
        public void handleEvent(Usermessages.GetUsersRequest event, String transactionId)
        {
            Message response;
            try
            {
                List<UserOuterClass.User> users = _userBll.getUsers(event.getOrganizationId());
                response = Usermessages.GetUsersResponse
                        .newBuilder()
                        .addAllUsers(users)
                        .build();
            }
            catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    // In: Usermessages.LoginUserRequest
    // Out: Usermessages.UserResponse
    class LoginUserEventHandler implements EventHandler<Usermessages.LoginUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.LoginUserRequest event, String transactionId)
        {
            Message response;
            try
            {
                UserOuterClass.User user = _userBll.authUser(event.getUser().getCellPhone(), event.getUser().getEmail(), event.getUser().getPassword());
                response = Usermessages.UserResponse
                        .newBuilder()
                        .setUser(user)
                        .build();
            }
            catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Usermessages.UserResponse.class);
            }

            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response with transaction id: {} and message type: {}", transactionId, response.getClass());
        }
    }
}
