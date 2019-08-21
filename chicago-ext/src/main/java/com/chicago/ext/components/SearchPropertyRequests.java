package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.AddressOuterClass;
import com.chicago.dto.Addressmessages;
import com.chicago.dto.Searchfiltersmessages;
import com.chicago.dto.Usermessages;
import com.chicago.ext.bll.AddressBll;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SearchPropertyRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchPropertyRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private AddressBll _addressBll;

    public SearchPropertyRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Addressmessages.AddressRequest.class, new AddressEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Searchfiltersmessages.SearchFiltersResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class AddressEventHandler implements EventHandler<Addressmessages.AddressRequest>
    {
        @Override
        public void handleEvent(Addressmessages.AddressRequest event, String transactionId)
        {
            Message response;
            try
            {
                AddressOuterClass.Address address = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        address = _addressBll.createAddress(event.getAddress());
                        break;
                    }
                    case READ:
                    {
                        address = _addressBll.getAddress(event.getAddress().getAddressId());
                        break;
                    }
                    case UPDATE:
                    {
                        _addressBll.updateAddress(event.getAddress());
                        address = AddressOuterClass.Address.newBuilder(event.getAddress()).build();
                        break;
                    }
                }

                response = Addressmessages.AddressResponse
                        .newBuilder()
                        .setAddress(address)
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Usermessages.UserResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
