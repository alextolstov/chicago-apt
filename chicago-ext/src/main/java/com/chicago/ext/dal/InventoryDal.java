package com.chicago.ext.dal;

import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface InventoryDal
{
    // Inventory
    String createInventory(String organizationId) throws Exception;

    void updateInventory(OrganizationOuterClass.Organization organization);

    // Item
    String createInventoryItem(Inventory.InventoryItem inventoryItem);

    void updateInventoryItem(Inventory.InventoryItem inventoryItem);

    Inventory.InventoryItem getInventoryItem(String itemId) throws Exception;

    List<Inventory.InventoryItem> getInventoryItems(String entityId) throws Exception;

    // Operations
    void applyInventoryOperation(Inventory.InventoryOperation inventoryOperation);

    void startInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);

    void acceptInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);

    void rejectInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);

    // Brands
    Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand);

    void updateItemBrand(Inventory.InventoryItemBrand brand);

    Map<UUID, String> getItemBrands(String entityId);

    // Categories
    Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category);

    void updateItemCategory(Inventory.InventoryItemCategory category);

    Map<UUID, String> getItemCategories(String entityId);

    // Units
    Inventory.InventoryItemUnit createItemUnit(Inventory.InventoryItemUnit unit);

    void updateItemUnit(Inventory.InventoryItemUnit unit);

    Map<UUID, String> getItemUnits(String entityId);

    // Suppliers
    Inventory.InventoryItemSupplier createItemSupplier(Inventory.InventoryItemSupplier supplier);

    void updateItemSupplier(Inventory.InventoryItemSupplier supplier);

    Map<UUID, String> getItemSuppliers(String entityId);
}
