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

    // Units
    Inventory.InventoryItemUnit createItemUnit(Inventory.InventoryItemUnit unit);

    void updateItemUnit(Inventory.InventoryItemUnit unit);

    Inventory.InventoryItemUnits getItemUnits(String entityId);

    // Suppliers
    Inventory.InventoryItemSupplier createItemUnitSupplier(Inventory.InventoryItemSupplier supplier);

    void updateItemSupplier(Inventory.InventoryItemSupplier supplier);

    Inventory.InventoryItemSuppliers getItemSuppliers(String entityId);

}


