package com.chicago.ext.dal;

import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;

import java.util.Map;
import java.util.UUID;

public interface InventoryDal
{
    String createInventory(String organizationId) throws Exception;

    void updateInventory(OrganizationOuterClass.Organization organization);

    Inventory.InventoryItem createInventoryItem(Inventory.InventoryItem inventoryItem);

    void updateInventoryItem(Inventory.InventoryItem inventoryItem);

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
    Inventory.InventoryItemSupplier createItemUnitSupplier(Inventory.InventoryItemSupplier supplier);

    void updateItemSupplier(Inventory.InventoryItemSupplier supplier);

    Map<UUID, String> getItemSuppliers(String entityId);
}
