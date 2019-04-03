package com.chicago.ext.dal;

import com.chicago.dto.InventoryOuterClass;
import com.chicago.dto.OrganizationOuterClass;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface InventoryDal
{
    // Inventory
    String createInventory(InventoryOuterClass.Inventory inventory) throws Exception;

    void updateInventory(InventoryOuterClass.Inventory inventory);

    InventoryOuterClass.Inventory getInventory(String organizationId) throws Exception;

    // Catalog Item
    String createInventoryCatalogItem(InventoryOuterClass.InventoryCatalogItem inventoryItem);

    void updateInventoryCatalogItem(InventoryOuterClass.InventoryCatalogItem inventoryItem);

    InventoryOuterClass.InventoryCatalogItem getInventoryCatalogItem(String itemId) throws Exception;

    List<InventoryOuterClass.InventoryCatalogItem> getInventoryCatalogItems(String entityId) throws Exception;

    // Operations
    void applyInventoryOperation(InventoryOuterClass.InventoryOperation inventoryOperation);

    void startInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer);

    void acceptInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer);

    void rejectInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer);

    // Brands
    InventoryOuterClass.InventoryItemBrand createItemBrand(InventoryOuterClass.InventoryItemBrand brand);

    void updateItemBrand(InventoryOuterClass.InventoryItemBrand brand);

    Map<UUID, String> getItemBrands(String entityId);

    // Categories
    InventoryOuterClass.InventoryItemCategory createItemCategory(InventoryOuterClass.InventoryItemCategory category);

    void updateItemCategory(InventoryOuterClass.InventoryItemCategory category);

    Map<UUID, String> getItemCategories(String entityId);

    // Units
    InventoryOuterClass.InventoryItemUnit createItemUnit(InventoryOuterClass.InventoryItemUnit unit);

    void updateItemUnit(InventoryOuterClass.InventoryItemUnit unit);

    Map<UUID, String> getItemUnits(String entityId);

    // Suppliers
    InventoryOuterClass.InventoryItemSupplier createItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier);

    void updateItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier);

    Map<UUID, String> getItemSuppliers(String entityId);

    // Locations
    InventoryOuterClass.InventoryLocation createInventoryLocation(InventoryOuterClass.InventoryLocation location);

    void updateInventoryLocation(InventoryOuterClass.InventoryLocation location);

    Map<UUID, String> getInventoryLocations(String entityId);
}
