package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.*;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.chicago.dto.Common;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.Positionmessages;
import com.chicago.dto.Usermessages;
import com.chicago.ext.bll.PositionBll;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PositionRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(PositionRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private PositionBll _positionBll;

    private AbstractEventDispatcher _ed;

    public PositionRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Positionmessages.PositionRequest.class, new PositionEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Positionmessages.PositionResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Positionmessages.PositionsResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _positionBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(PositionBll.class);
        LOG.info("Service locator found PositionBll");
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class PositionEventHandler implements EventHandler<Positionmessages.PositionRequest>
    {
        @Override
        public void handleEvent(Positionmessages.PositionRequest event, String transactionId)
        {
            Message response;
            try
            {
                Message posMsg = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        posMsg = _positionBll.createPosition(event.getPosition());
                        break;
                    }
                    case UPDATE:
                    {
                        _positionBll.updatePosition(event.getPosition());
                        posMsg = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                    case DELETE:
                    {
                        _positionBll.deletePosition(event.getPosition());
                        posMsg = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                    case READ:
                    {
                        posMsg = _positionBll.getPositions(event.getPosition().getOrganizationId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ)
                {
                    response = Positionmessages.PositionsResponse
                            .newBuilder()
                            .setPositions((PositionOuterClass.Positions)posMsg)
                            .build();
                }
                else
                {
                    response = Positionmessages.PositionResponse
                            .newBuilder()
                            .setPosition((PositionOuterClass.Position)posMsg)
                            .build();
                }
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
