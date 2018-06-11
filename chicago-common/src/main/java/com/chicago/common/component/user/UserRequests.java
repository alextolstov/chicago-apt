package com.chicago.common.component.user;

import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.Common;
import com.chicago.dto.Usermessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.google.protobuf.UnknownFieldSet.Field.newBuilder;

public class UserRequests extends AbstractComponent
{
    private static final Logger _LOG = LoggerFactory.getLogger(AbstractComponent.class);
    private AbstractEventDispatcher _ed;

    public UserRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.CreateUserRequest.class.getCanonicalName(), new CreateUserEventHandler());
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
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .build();
            }catch(Exception ex)
            {
                Common.TransactionError transactionError = Common.TransactionError
                        .newBuilder()
                        .setErrorCode(500)
                        .setErrorMessage(ex.getMessage())
                        .build();
                createUserResponse = Usermessages.CreateUserResponse
                        .newBuilder()
                        .setTransactionError(transactionError)
                        .build();
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), createUserResponse, transactionId));
        }
    }
}
