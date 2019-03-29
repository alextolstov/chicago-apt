package com.chicago.ext.bll;

import com.chicago.dto.InventoryOuterClass;
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
    public InventoryOuterClass.InventoryItemBrand createItemBrand(InventoryOuterClass.InventoryItemBrand brand)
    {
        return _inventoryDal.createItemBrand(brand);
    }

    @Override
    public void updateItemBrand(InventoryOuterClass.InventoryItemBrand brand)
    {
        _inventoryDal.updateItemBrand(brand);
    }

    @Override
    public List<InventoryOuterClass.InventoryItemBrand> getItemBrands(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemBrands(entityId);

        List<InventoryOuterClass.InventoryItemBrand> newResult = result.entrySet().stream().map(e -> InventoryOuterClass.InventoryItemBrand.newBuilder()
                .setBrandId(e.getKey().toString()).setBrandName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Categories
    @Override
    public InventoryOuterClass.InventoryItemCategory createItemCategory(InventoryOuterClass.InventoryItemCategory category)
    {
        return _inventoryDal.createItemCategory(category);
    }

    @Override
    public void updateItemCategory(InventoryOuterClass.InventoryItemCategory category)
    {
        _inventoryDal.updateItemCategory(category);
    }

    @Override
    public List<InventoryOuterClass.InventoryItemCategory> getItemCategories(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemCategories(entityId);
        List<InventoryOuterClass.InventoryItemCategory> newResult = result.entrySet().stream().map(e -> InventoryOuterClass.InventoryItemCategory.newBuilder()
                .setCategoryId(e.getKey().toString()).setCategoryName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Units
    @Override
    public InventoryOuterClass.InventoryItemUnit createItemUnit(InventoryOuterClass.InventoryItemUnit unit)
    {
        return _inventoryDal.createItemUnit(unit);
    }

    @Override
    public void updateItemUnit(InventoryOuterClass.InventoryItemUnit unit)
    {
        _inventoryDal.updateItemUnit(unit);
    }

    @Override
    public List<InventoryOuterClass.InventoryItemUnit> getItemUnits(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemUnits(entityId);
        List<InventoryOuterClass.InventoryItemUnit> newResult = result.entrySet().stream().map(e -> InventoryOuterClass.InventoryItemUnit.newBuilder()
                .setUnitId(e.getKey().toString()).setUnitName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Suppliers
    @Override
    public InventoryOuterClass.InventoryItemSupplier createItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier)
    {
        return _inventoryDal.createItemSupplier(supplier);
    }

    @Override
    public void updateItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier)
    {
        _inventoryDal.updateItemSupplier(supplier);
    }

    @Override
    public List<InventoryOuterClass.InventoryItemSupplier> getItemSuppliers(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemSuppliers(entityId);
        List<InventoryOuterClass.InventoryItemSupplier> newResult = result.entrySet().stream().map(e -> InventoryOuterClass.InventoryItemSupplier.newBuilder()
                .setSupplierId(e.getKey().toString()).setSupplierName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    // Locations
    @Override
    public InventoryOuterClass.InventoryLocation createInventoryLocation(InventoryOuterClass.InventoryLocation location)
    {
        return _inventoryDal.createInventoryLocation(location);
    }

    @Override
    public void updateInventoryLocation(InventoryOuterClass.InventoryLocation location)
    {
        _inventoryDal.updateInventoryLocation(location);
    }

    @Override
    public List<InventoryOuterClass.InventoryLocation> getInventoryLocations(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getInventoryLocations(entityId);
        List<InventoryOuterClass.InventoryLocation> newResult = result.entrySet().stream().map(e -> InventoryOuterClass.InventoryLocation.newBuilder()
                .setLocationId(e.getKey().toString()).setLocationName(e.getValue()).build()).collect(Collectors.toList());
        return newResult;
    }

    @Override
    public InventoryOuterClass.InventoryItem createInventoryItem(InventoryOuterClass.InventoryItem item)
    {
        String itemId = _inventoryDal.createInventoryItem(item);
        InventoryOuterClass.InventoryItem newItem = InventoryOuterClass.InventoryItem.newBuilder(item)
                .setItemId(itemId).build();
        return newItem;
    }

    @Override
    public void updateInventoryItem(InventoryOuterClass.InventoryItem item)
    {
        _inventoryDal.updateInventoryItem(item);
    }

    @Override
    public InventoryOuterClass.InventoryItem getInventoryItem(String itemId) throws Exception
    {
        return _inventoryDal.getInventoryItem(itemId);
    }

    @Override
    public List<InventoryOuterClass.InventoryItem> getInventoryItems(String entityId, String inventoryId) throws Exception
    {
        return _inventoryDal.getInventoryItems(entityId, inventoryId);
     }
}


