package com.chicago.ext.bll;

import com.chicago.dto.Inventory;
import com.chicago.ext.dal.InventoryDal;
import org.jvnet.hk2.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Contract
public class InventoryBllImpl implements InventoryBll
{
    private static final Logger LOG = LoggerFactory.getLogger(InventoryBllImpl.class);
    @Inject
    InventoryDal _inventoryDal;

    // Brands
    @Override
    public Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand)
    {
        return _inventoryDal.createItemBrand(brand);
    }

    @Override
    public void updateItemBrand(Inventory.InventoryItemBrand brand)
    {
        _inventoryDal.updateItemBrand(brand);
    }

    @Override
    public List<Inventory.InventoryItemBrand> getItemBrands(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemBrands(entityId);

        List<Inventory.InventoryItemBrand> newResult = result.entrySet().stream().map(e -> Inventory.InventoryItemBrand.newBuilder()
                .setBrandId(e.getKey().toString()).setBrandName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Categories
    @Override
    public Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category)
    {
        return _inventoryDal.createItemCategory(category);
    }

    @Override
    public void updateItemCategory(Inventory.InventoryItemCategory category)
    {
        _inventoryDal.updateItemCategory(category);
    }

    @Override
    public List<Inventory.InventoryItemCategory> getItemCategories(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemCategories(entityId);
        List<Inventory.InventoryItemCategory> newResult = result.entrySet().stream().map(e -> Inventory.InventoryItemCategory.newBuilder()
                .setCategoryId(e.getKey().toString()).setCategoryName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Units
    @Override
    public Inventory.InventoryItemUnit createItemUnit(Inventory.InventoryItemUnit unit)
    {
        return _inventoryDal.createItemUnit(unit);
    }

    @Override
    public void updateItemUnit(Inventory.InventoryItemUnit unit)
    {
        _inventoryDal.updateItemUnit(unit);
    }

    @Override
    public List<Inventory.InventoryItemUnit> getItemUnits(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemUnits(entityId);
        List<Inventory.InventoryItemUnit> newResult = result.entrySet().stream().map(e -> Inventory.InventoryItemUnit.newBuilder()
                .setUnitId(e.getKey().toString()).setUnitName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Suppliers
    @Override
    public Inventory.InventoryItemSupplier createItemSupplier(Inventory.InventoryItemSupplier supplier)
    {
        return _inventoryDal.createItemSupplier(supplier);
    }

    @Override
    public void updateItemSupplier(Inventory.InventoryItemSupplier supplier)
    {
        _inventoryDal.updateItemSupplier(supplier);
    }

    @Override
    public List<Inventory.InventoryItemSupplier> getItemSuppliers(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemSuppliers(entityId);
        List<Inventory.InventoryItemSupplier> newResult = result.entrySet().stream().map(e -> Inventory.InventoryItemSupplier.newBuilder()
                .setSupplierId(e.getKey().toString()).setSupplierName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Locations
    @Override
    public Inventory.InventoryLocation createInventoryLocation(Inventory.InventoryLocation location)
    {
        return _inventoryDal.createInventoryLocation(location);
    }

    @Override
    public void updateInventoryLocation(Inventory.InventoryLocation location)
    {
        _inventoryDal.updateInventoryLocation(location);
    }

    @Override
    public List<Inventory.InventoryLocation> getInventoryLocations(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getInventoryLocations(entityId);
        List<Inventory.InventoryLocation> newResult = result.entrySet().stream().map(e -> Inventory.InventoryLocation.newBuilder()
                .setLocationId(e.getKey().toString()).setLocationName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    @Override
    public Inventory.InventoryItem createInventoryItem(Inventory.InventoryItem item)
    {
        String itemId = _inventoryDal.createInventoryItem(item);
        Inventory.InventoryItem newItem = Inventory.InventoryItem.newBuilder(item)
                .setItemId(itemId).build();
        return newItem;
    }

    @Override
    public void updateInventoryItem(Inventory.InventoryItem item)
    {
        _inventoryDal.updateInventoryItem(item);
    }

    @Override
    public Inventory.InventoryItem getInventoryItem(String itemId) throws Exception
    {
        return _inventoryDal.getInventoryItem(itemId);
    }

    @Override
    public List<Inventory.InventoryItem> getInventoryItems(String entityId) throws Exception
    {
        return _inventoryDal.getInventoryItems(entityId);
     }
}


