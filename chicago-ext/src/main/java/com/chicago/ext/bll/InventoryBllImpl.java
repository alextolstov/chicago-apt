package com.chicago.ext.bll;

import com.chicago.dto.Inventory;
import com.chicago.ext.dal.InventoryDal;
import org.jvnet.hk2.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
    public Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand)
    {
        return _inventoryDal.createItemBrand(brand);
    }

    public void updateItemBrand(Inventory.InventoryItemBrand brand)
    {
        _inventoryDal.updateItemBrand(brand);
    }

    public Inventory.InventoryItemBrands getItemBrands(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemBrands(entityId);
        Map<String, String> newResult = result.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        return Inventory.InventoryItemBrands.newBuilder()
                .setEntityId(entityId)
                .putAllBrands(newResult)
                .build();
    }

    // Categories
    public Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category)
    {
        return _inventoryDal.createItemCategory(category);
    }

    public void updateItemCategory(Inventory.InventoryItemCategory category)
    {
        _inventoryDal.updateItemCategory(category);
    }

    public Inventory.InventoryItemCategories getItemCategories(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemCategories(entityId);
        Map<String, String> newResult = result.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        return Inventory.InventoryItemCategories.newBuilder()
                .setEntityId(entityId)
                .putAllCategories(newResult)
                .build();
    }

    // Measurements
    public Inventory.InventoryItemMeasurement createItemMeasurement(Inventory.InventoryItemMeasurement measurement)
    {
        return _inventoryDal.createItemMeasurement(measurement);
    }

    public void updateItemMeasurement(Inventory.InventoryItemMeasurement measurement)
    {
        _inventoryDal.updateItemMeasurement(measurement);
    }

    public Inventory.InventoryItemMeasurements getItemMeasurements(String entityId)
    {
        Map<UUID, String> result = _inventoryDal.getItemMeasurements(entityId);
        Map<String, String> newResult = result.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        return Inventory.InventoryItemMeasurements.newBuilder()
                .setEntityId(entityId)
                .putAllMeasurements(newResult)
                .build();
    }
}


