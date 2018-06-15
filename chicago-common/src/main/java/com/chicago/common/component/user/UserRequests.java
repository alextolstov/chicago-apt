package com.chicago.common.component.user;

import com.chicago.common.bll.UserBll;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.common.dal.cassandra.PasswordNotMatchException;
import com.chicago.common.dal.cassandra.UserNotFoundException;
import com.chicago.common.util.ErrorResponseUtil;
import com.chicago.dto.Usermessages;
import org.apache.http.HttpStatus;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UserRequests extends AbstractComponent
{
    private static final Logger _LOG = LoggerFactory.getLogger(AbstractComponent.class);
    private UserBll _userBll;


    private AbstractEventDispatcher _ed;

    public UserRequests(ComponentManager cm) throws ClassNotFoundException
    {
        ServiceLocator locator = ServiceLocatorFactory.getInstance().find("servicelocator");
        _userBll = locator.getService(UserBll.class);
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.CreateUserRequest.class.getCanonicalName(), new CreateUserEventHandler());
        _ed.registerHandler(Usermessages.LoginUserRequest.class.getCanonicalName(), new LoginUserEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class CreateUserEventHandler implements EventHandler<Usermessages.CreateUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.CreateUserRequest event, String transactionId)
        {
            Usermessages.CreateUserResponse createUserResponse;
            try
            {
                _userBll.createFirstUser(event.getUser());
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .build();
            } catch (Exception ex)
            {
                _LOG.error(ex.getMessage());
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .setTransactionError(ErrorResponseUtil.createErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getMessage()))
                        .build();
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), createUserResponse, transactionId));
        }
    }

    class LoginUserEventHandler implements EventHandler<Usermessages.LoginUserRequest>
    {
        @Override
        public void handleEvent(Usermessages.LoginUserRequest event, String transactionId)
        {
            Usermessages.LoginUserResponse loginUserResponse;
            try
            {
                _userBll.authUser(event.getUser().getEmail(), event.getUser().getPassword());
                loginUserResponse = Usermessages.LoginUserResponse
                        .newBuilder()
                        .build();
            } catch (UserNotFoundException ex)
            {
                _LOG.error(ex.getMessage());
                loginUserResponse = Usermessages.LoginUserResponse
                        .newBuilder()
                        .setTransactionError(ErrorResponseUtil.createErrorResponse(HttpStatus.SC_NOT_FOUND, ex.getMessage()))
                        .build();
            } catch (PasswordNotMatchException ex)
            {
                _LOG.error(ex.getMessage());
                loginUserResponse = Usermessages.LoginUserResponse
                        .newBuilder()
                        .setTransactionError(ErrorResponseUtil.createErrorResponse(HttpStatus.SC_UNAUTHORIZED, ex.getMessage()))
                        .build();
            } catch (Exception ex)
            {
                _LOG.error(ex.getMessage());
                loginUserResponse = Usermessages.LoginUserResponse
                        .newBuilder()
                        .setTransactionError(ErrorResponseUtil.createErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getMessage()))
                        .build();
            }

            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), loginUserResponse, transactionId));
        }
    }
}
