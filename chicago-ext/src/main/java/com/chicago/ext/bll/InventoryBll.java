package com.chicago.ext.bll;

import com.chicago.dto.Inventory;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface InventoryBll
{
    // Brands
    Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand);

    void updateItemBrand(Inventory.InventoryItemBrand brand);

    List<Inventory.InventoryItemBrand> getItemBrands(String entityId);

    // Categories
    Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category);

    void updateItemCategory(Inventory.InventoryItemCategory category);

    List<Inventory.InventoryItemCategory> getItemCategories(String entityId);

    // Units
    Inventory.InventoryItemUnit createItemUnit(Inventory.InventoryItemUnit unit);

    void updateItemUnit(Inventory.InventoryItemUnit unit);

    List<Inventory.InventoryItemUnit> getItemUnits(String entityId);

    // Suppliers
    Inventory.InventoryItemSupplier createItemSupplier(Inventory.InventoryItemSupplier supplier);

    void updateItemSupplier(Inventory.InventoryItemSupplier supplier);

    List<Inventory.InventoryItemSupplier> getItemSuppliers(String entityId);

    // Items
    Inventory.InventoryItem createInventoryItem(Inventory.InventoryItem item);

    void updateInventoryItem(Inventory.InventoryItem item);

    Inventory.InventoryItem getInventoryItem(String itemId);

    List<Inventory.InventoryItem> getInventoryItems(String entityId);
}
