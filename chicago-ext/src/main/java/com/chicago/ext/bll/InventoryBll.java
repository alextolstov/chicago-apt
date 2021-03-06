package com.chicago.ext.bll;

import com.chicago.dto.InventoryOuterClass;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface InventoryBll
{
    // Brands
    InventoryOuterClass.InventoryItemBrand createItemBrand(InventoryOuterClass.InventoryItemBrand brand);

    void updateItemBrand(InventoryOuterClass.InventoryItemBrand brand);

    List<InventoryOuterClass.InventoryItemBrand> getItemBrands(String entityId);

    // Categories
    InventoryOuterClass.InventoryItemCategory createItemCategory(InventoryOuterClass.InventoryItemCategory category);

    void updateItemCategory(InventoryOuterClass.InventoryItemCategory category);

    List<InventoryOuterClass.InventoryItemCategory> getItemCategories(String entityId);

    // Units
    InventoryOuterClass.InventoryItemUnit createItemUnit(InventoryOuterClass.InventoryItemUnit unit);

    void updateItemUnit(InventoryOuterClass.InventoryItemUnit unit);

    List<InventoryOuterClass.InventoryItemUnit> getItemUnits(String entityId);

    // Suppliers
    InventoryOuterClass.InventoryItemSupplier createItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier);

    void updateItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier);

    List<InventoryOuterClass.InventoryItemSupplier> getItemSuppliers(String entityId);

    // Locations
    InventoryOuterClass.InventoryLocation createInventoryLocation(InventoryOuterClass.InventoryLocation location);

    void updateInventoryLocation(InventoryOuterClass.InventoryLocation location);

    List<InventoryOuterClass.InventoryLocation> getInventoryLocations(String entityId);

    // Catalog Items
    InventoryOuterClass.InventoryCatalogItem createInventoryCatalogItem(InventoryOuterClass.InventoryCatalogItem item);

    void updateInventoryCatalogItem(InventoryOuterClass.InventoryCatalogItem item);

    InventoryOuterClass.InventoryCatalogItem getInventoryCatalogItem(String itemId) throws Exception;

    List<InventoryOuterClass.InventoryCatalogItem> getInventoryCatalogItems(String entityId) throws Exception;
}
