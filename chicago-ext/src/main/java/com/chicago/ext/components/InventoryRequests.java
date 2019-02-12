package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.Common;
import com.chicago.dto.Inventory;
import com.chicago.dto.Inventorymessages;
import com.chicago.dto.PositionOuterClass;
import com.chicago.dto.Positionmessages;
import com.chicago.dto.Usermessages;
import com.chicago.ext.bll.InventoryBll;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class InventoryRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(InventoryRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private InventoryBll _inventoryBll;

    public InventoryRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandRequest.class, new InventoryItemBrandEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemCategoryRequest.class, new InventoryItemCategoryEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandsResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _inventoryBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(InventoryBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class InventoryItemBrandEventHandler implements EventHandler<Inventorymessages.InventoryItemBrandRequest>
    {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemBrandRequest event, String transactionId)
        {
            Message response;
            try
            {
                Message dataMsg = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        dataMsg = _inventoryBll.createItemBrand(event.getItemBrand());
                        break;
                    }
                    case UPDATE:
                    {
                        _inventoryBll.updateItemBrand(event.getItemBrand());
                        dataMsg = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        dataMsg = _inventoryBll.getItemBrands(event.getItemBrand().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ)
                {
                    response = Inventorymessages.InventoryItemBrandsResponse
                            .newBuilder()
                            .setItemBrands((Inventory.InventoryItemBrands) dataMsg)
                            .build();
                } else
                {
                    response = Inventorymessages.InventoryItemBrandResponse
                            .newBuilder()
                            .setItemBrand((Inventory.InventoryItemBrand) dataMsg)
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

    class InventoryItemCategoryEventHandler implements EventHandler<Inventorymessages.InventoryItemCategoryRequest>
    {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemCategoryRequest event, String transactionId)
        {
            Message response;
            try
            {
                Message dataMsg = null;

                switch (event.getCrudOperation())
                {
                    case CREATE:
                    {
                        dataMsg = _inventoryBll.createItemCategory(event.getItemCategory());
                        break;
                    }
                    case UPDATE:
                    {
                        _inventoryBll.updateItemCategory(event.getItemCategory());
                        dataMsg = PositionOuterClass.Position.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        dataMsg = _inventoryBll.getItemCategories(event.getItemCategory().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ)
                {
                    response = Inventorymessages.InventoryItemCategoriesResponse
                            .newBuilder()
                            .setItemCategories((Inventory.InventoryItemCategories) dataMsg)
                            .build();
                } else
                {
                    response = Inventorymessages.InventoryItemCategoryResponse
                            .newBuilder()
                            .setItemCategory((Inventory.InventoryItemCategory) dataMsg)
                            .build();
                }
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
