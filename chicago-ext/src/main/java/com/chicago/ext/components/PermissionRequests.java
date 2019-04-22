package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.Permissionmessages;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.bll.PermissionBll;
import com.google.protobuf.Message;
import org.apache.http.HttpStatus;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class PermissionRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private PermissionBll _permissionBll;

    public PermissionRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Permissionmessages.UserPermissionsRequest.class, new UserPermissionsEventHandler());
        _ed.registerHandler(Permissionmessages.SystemRolesRequest.class, new SystemPermissionsEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Permissionmessages.SystemRolesResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Permissionmessages.UserPermissionsResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _permissionBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(PermissionBll.class);
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
                        permissionsMsg = UserOuterClass.UserPermissions.getDefaultInstance();
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
                response = ResponseFactoryUtil.createErrorResponse(ex, Permissionmessages.UserPermissionsResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class SystemPermissionsEventHandler implements EventHandler<Permissionmessages.SystemRolesRequest>
    {
        @Override
        public void handleEvent(Permissionmessages.SystemRolesRequest event, String transactionId)
        {
            Message response;
            try
            {
                List<PermissionOuterClass.Role> permissionsMsg = _permissionBll.getSystemRoles();

                response = Permissionmessages.SystemRolesResponse
                        .newBuilder()
                        .addAllRoles(permissionsMsg)
                        .build();

            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Permissionmessages.SystemRolesResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
