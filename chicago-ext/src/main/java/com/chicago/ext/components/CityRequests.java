package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.CityOuterClass;
import com.chicago.dto.Citymessages;
import com.chicago.dto.Common;
import com.chicago.ext.bll.CityBll;
import com.chicago.ext.model.CityModel;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CityRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(CityRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private CityBll _cityBll;

    public CityRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Citymessages.CityRequest.class, new CityEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Citymessages.CityResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _cityBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(CityBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class CityEventHandler implements EventHandler<Citymessages.CityRequest>
    {
        @Override
        public void handleEvent(Citymessages.CityRequest event, String transactionId)
        {
            Message response;
            try
            {
                CityOuterClass.City cityDto = null;

                switch (event.getCrudOperation())
                {
                    case READ:
                    {
                        CityModel.City cityModel = _cityBll.getCity(event.getCity().getCityId());
                        cityDto = new CityModel.CityConvertor().toDto(cityModel);
                        break;
                    }
                }

                response = Citymessages.CityResponse
                        .newBuilder()
                        .setCity(cityDto)
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
