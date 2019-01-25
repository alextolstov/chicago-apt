package com.chicago.ext.bll;

import com.chicago.dto.Inventory;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface InventoryBll
{
    // Brands
    Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand);

    void updateItemBrand(Inventory.InventoryItemBrand brand);

    Inventory.InventoryItemBrands getItemBrands(String entityId);

    // Categories
    Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category);

    void updateItemCategory(Inventory.InventoryItemCategory category);

    Inventory.InventoryItemCategories getItemCategories(String entityId);

    // Measurements
    Inventory.InventoryItemMeasurement createItemMeasurement(Inventory.InventoryItemMeasurement measurement);

    void updateItemMeasurement(Inventory.InventoryItemMeasurement measurement);

    Inventory.InventoryItemMeasurements getItemMeasurements(String entityId);
}


