package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.Common;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.dto.Organizationmessages;
import com.chicago.ext.bll.OrganizationBll;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class OrganizationRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(OrganizationRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private OrganizationBll _organizationBll;

    public OrganizationRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Organizationmessages.OrganizationRequest.class, new OrganizationEventHandler());
        _ed.registerHandler(Organizationmessages.OrganizationStructureRequest.class, new OrganizationStructureEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Organizationmessages.OrganizationResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Organizationmessages.OrganizationStructureResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _organizationBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(OrganizationBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class OrganizationEventHandler implements EventHandler<Organizationmessages.OrganizationRequest>
    {
        @Override
        public void handleEvent(Organizationmessages.OrganizationRequest event, String transactionId)
        {
            Message response;
            try
            {
                OrganizationOuterClass.Organization organization = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        organization = _organizationBll.createOrganization(event.getOrganization());
                        break;
                    }
                    case READ:
                    {
                        organization = _organizationBll.getOrganization(event.getOrganization());
                        break;
                    }
                    case UPDATE:
                    {
                        _organizationBll.updateOrganization(event.getOrganization());
                        organization = OrganizationOuterClass.Organization.getDefaultInstance();
                        break;
                    }
                }

                response = Organizationmessages.OrganizationResponse.newBuilder()
                        .setOrganization(organization)
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class OrganizationStructureEventHandler implements EventHandler<Organizationmessages.OrganizationStructureRequest>
    {
        @Override
        public void handleEvent(Organizationmessages.OrganizationStructureRequest event, String transactionId)
        {
            Message response;
            try
            {
                OrganizationOuterClass.OrganizationInfo organizationInfo = _organizationBll.getOrganizationStructure(event.getUserId(), event.getOrganizationType());
                response = Organizationmessages.OrganizationStructureResponse.newBuilder()
                        .setOrganizationInfo(organizationInfo)
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
