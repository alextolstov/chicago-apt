package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.Common;
import com.chicago.dto.InventoryOuterClass;
import com.chicago.dto.Inventorymessages;
import com.chicago.ext.bll.InventoryBll;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryRequests extends AbstractComponent {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private InventoryBll _inventoryBll;

    public InventoryRequests(ComponentManager cm) throws ClassNotFoundException {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Inventorymessages.InventoryCatalogItemRequest.class, new InventoryCatalogItemEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryCatalogItemsRequest.class, new InventoryCatalogItemsEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemCategoryRequest.class, new InventoryItemCategoryEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandRequest.class, new InventoryItemBrandEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemUnitRequest.class, new InventoryItemUnitEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemSupplierRequest.class, new InventoryItemSupplierEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryLocationRequest.class, new InventoryLocationEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Inventorymessages.InventoryCatalogItemResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryCatalogItemsResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemCategoryResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemCategoriesResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemBrandsResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemUnitResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemUnitsResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemSupplierResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Inventorymessages.InventoryItemSuppliersResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca) {
        _inventoryBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(InventoryBll.class);
        return true;
    }

    public static void registerComponentFactories() {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class InventoryCatalogItemEventHandler implements EventHandler<Inventorymessages.InventoryCatalogItemRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryCatalogItemRequest event, String transactionId) {
            Message response;
            try {
                InventoryOuterClass.InventoryCatalogItem item = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        item = _inventoryBll.createInventoryCatalogItem(event.getInventoryItem());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateInventoryCatalogItem(event.getInventoryItem());
                        item = InventoryOuterClass.InventoryCatalogItem.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all items
                    {
                        item = _inventoryBll.getInventoryCatalogItem(event.getInventoryItem().getItemId());
                        break;
                    }
                }

                response = Inventorymessages.InventoryCatalogItemResponse
                        .newBuilder()
                        .setInventoryItem(item)
                        .build();
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryCatalogItemsEventHandler implements EventHandler<Inventorymessages.InventoryCatalogItemsRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryCatalogItemsRequest event, String transactionId) {
            Message response;
            try {
                List<InventoryOuterClass.InventoryCatalogItem> items = _inventoryBll.getInventoryCatalogItems(event.getEntityId());
                response = Inventorymessages.InventoryCatalogItemsResponse
                        .newBuilder()
                        .addAllInventoryItems(items)
                        .build();
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryItemCategoryEventHandler implements EventHandler<Inventorymessages.InventoryItemCategoryRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemCategoryRequest event, String transactionId) {
            Message response;
            try {
                Message dataMsg = null;
                List<InventoryOuterClass.InventoryItemCategory> categories = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        dataMsg = _inventoryBll.createItemCategory(event.getItemCategory());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateItemCategory(event.getItemCategory());
                        dataMsg = InventoryOuterClass.InventoryItemCategory.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        categories = _inventoryBll.getItemCategories(event.getItemCategory().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ) {
                    response = Inventorymessages.InventoryItemCategoriesResponse
                            .newBuilder()
                            .addAllItemCategories(categories)
                            .build();
                } else {
                    response = Inventorymessages.InventoryItemCategoryResponse
                            .newBuilder()
                            .setItemCategory((InventoryOuterClass.InventoryItemCategory) dataMsg)
                            .build();
                }
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryItemBrandEventHandler implements EventHandler<Inventorymessages.InventoryItemBrandRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemBrandRequest event, String transactionId) {
            Message response;
            try {
                Message dataMsg = null;
                List<InventoryOuterClass.InventoryItemBrand> brands = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        dataMsg = _inventoryBll.createItemBrand(event.getItemBrand());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateItemBrand(event.getItemBrand());
                        dataMsg = InventoryOuterClass.InventoryItemBrand.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        brands = _inventoryBll.getItemBrands(event.getItemBrand().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ) {
                    response = Inventorymessages.InventoryItemBrandsResponse
                            .newBuilder()
                            .addAllItemBrands(brands)
                            .build();
                } else {
                    response = Inventorymessages.InventoryItemBrandResponse
                            .newBuilder()
                            .setItemBrand((InventoryOuterClass.InventoryItemBrand) dataMsg)
                            .build();
                }
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryItemUnitEventHandler implements EventHandler<Inventorymessages.InventoryItemUnitRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemUnitRequest event, String transactionId) {
            Message response;
            try {
                Message dataMsg = null;
                List<InventoryOuterClass.InventoryItemUnit> units = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        dataMsg = _inventoryBll.createItemUnit(event.getItemUnit());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateItemUnit(event.getItemUnit());
                        dataMsg = InventoryOuterClass.InventoryItemUnit.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        units = _inventoryBll.getItemUnits(event.getItemUnit().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ) {
                    response = Inventorymessages.InventoryItemUnitsResponse
                            .newBuilder()
                            .addAllItemUnits(units)
                            .build();
                } else {
                    response = Inventorymessages.InventoryItemUnitResponse
                            .newBuilder()
                            .setItemUnit((InventoryOuterClass.InventoryItemUnit) dataMsg)
                            .build();
                }
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryItemSupplierEventHandler implements EventHandler<Inventorymessages.InventoryItemSupplierRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryItemSupplierRequest event, String transactionId) {
            Message response;
            try {
                Message dataMsg = null;
                List<InventoryOuterClass.InventoryItemSupplier> suppliers = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        dataMsg = _inventoryBll.createItemSupplier(event.getItemSupplier());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateItemSupplier(event.getItemSupplier());
                        dataMsg = InventoryOuterClass.InventoryItemSupplier.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        suppliers = _inventoryBll.getItemSuppliers(event.getItemSupplier().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ) {
                    response = Inventorymessages.InventoryItemSuppliersResponse
                            .newBuilder()
                            .addAllItemSuppliers(suppliers)
                            .build();
                } else {
                    response = Inventorymessages.InventoryItemBrandResponse
                            .newBuilder()
                            .setItemBrand((InventoryOuterClass.InventoryItemBrand) dataMsg)
                            .build();
                }
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class InventoryLocationEventHandler implements EventHandler<Inventorymessages.InventoryLocationRequest> {
        @Override
        public void handleEvent(Inventorymessages.InventoryLocationRequest event, String transactionId) {
            Message response;
            try {
                Message dataMsg = null;
                List<InventoryOuterClass.InventoryLocation> locations = null;

                switch (event.getCrudOperation()) {
                    case CREATE: {
                        dataMsg = _inventoryBll.createInventoryLocation(event.getInventoryLocation());
                        break;
                    }
                    case UPDATE: {
                        _inventoryBll.updateInventoryLocation(event.getInventoryLocation());
                        dataMsg = InventoryOuterClass.InventoryItemSupplier.getDefaultInstance();
                        break;
                    }
                    case READ: // On read return all brands
                    {
                        locations = _inventoryBll.getInventoryLocations(event.getInventoryLocation().getEntityId());
                        break;
                    }
                }

                if (event.getCrudOperation() == Common.CrudOperation.READ) {
                    response = Inventorymessages.InventoryLocationsResponse
                            .newBuilder()
                            .addAllInventoryLocations(locations)
                            .build();
                } else {
                    response = Inventorymessages.InventoryLocationResponse
                            .newBuilder()
                            .setInventoryLocation((InventoryOuterClass.InventoryLocation) dataMsg)
                            .build();
                }
            } catch (Exception ex) {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
