package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.common.util.ResponseFactoryUtil;
import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.Permissionmessages;
import com.chicago.dto.Positionmessages;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.bll.PermissionBll;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PermissionRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private PermissionBll _permissionBll;

    private AbstractEventDispatcher _ed;

    public PermissionRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Permissionmessages.UserPermissionsRequest.class, new UserPermissionsEventHandler());
        _ed.registerHandler(Permissionmessages.SystemPermissionsRequest.class, new SystemPermissionsEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Positionmessages.PositionResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Positionmessages.PositionsResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _permissionBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(PermissionBll.class);
        LOG.info("Service locator found PermissionBll");
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class UserPermissionsEventHandler implements EventHandler<Permissionmessages.UserPermissionsRequest>
    {
        @Override
        public void handleEvent(Permissionmessages.UserPermissionsRequest event, String transactionId)
        {
            Message response;
            try
            {
                Message permissionsMsg = null;

                switch (event.getCrudOperation())
                {
                    case UPDATE:
                    {
                        _permissionBll.setUserPermissions(event.getPermissions());
                        break;
                    }
                    case READ:
                    {
                        permissionsMsg = _permissionBll.getUserPermissions(event.getPermissions().getUserId());
                        break;
                    }
                }

                response = Permissionmessages.UserPermissionsResponse
                        .newBuilder()
                        .setPermissions((UserOuterClass.UserPermissions) permissionsMsg)
                        .build();

            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Permissionmessages.UserPermissionsResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class SystemPermissionsEventHandler implements EventHandler<Permissionmessages.SystemPermissionsRequest>
    {
        @Override
        public void handleEvent(Permissionmessages.SystemPermissionsRequest event, String transactionId)
        {
            Message response;
            try
            {
                PermissionOuterClass.Roles permissionsMsg = _permissionBll.getSystemPermissions();

                response = Permissionmessages.SystemPermissionsResponse
                        .newBuilder()
                        .setRoles(permissionsMsg)
                        .build();

            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        Permissionmessages.SystemPermissionsResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
