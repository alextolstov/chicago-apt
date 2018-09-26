package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.*;
import com.chicago.common.util.ResponseFactoryUtil;
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
        _ed.registerHandler(Positionmessages.PositionsRequest.class, new PositionsEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Positionmessages.PositionResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Positionmessages.PositionsResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _positionBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(PositionBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class PositionsEventHandler implements EventHandler<Positionmessages.PositionsRequest>
    {
        @Override
        public void handleEvent(Positionmessages.PositionsRequest event, String transactionId)
        {
            Message response;
            try
            {
                PositionOuterClass.Positions positions = _positionBll.getPositions(event.getOrganizationId());
                response = Positionmessages.PositionsResponse
                        .newBuilder()
                        .setPositions(positions)
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

    class PositionEventHandler implements EventHandler<Positionmessages.PositionRequest>
    {
        @Override
        public void handleEvent(Positionmessages.PositionRequest event, String transactionId)
        {
            Message response;
            try
            {
                PositionOuterClass.Position position = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        position = _positionBll.createPosition(event.getPosition());
                        break;
                    }
                    case UPDATE:
                    {
                        _positionBll.updatePosition(event.getPosition());
                        position = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                    case DELETE:
                    {
                        _positionBll.deletePosition(event.getPosition());
                        position = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                }

                response = Positionmessages.PositionResponse
                        .newBuilder()
                        .setPosition(position)
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
}
