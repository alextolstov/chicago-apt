package com.chicago.ext.dal.cassandra;

import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.ext.dal.InventoryDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_BRANDS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_CATEGORIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_MEASUREMENTS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.POSITIONS_TABLE;

public class InventoryDalImpl implements InventoryDal
{
    private static final Logger LOG = LoggerFactory.getLogger(InventoryDalImpl.class);

    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public String createInventory(String organizationId) throws Exception
    {
        return null;
    }

    @Override
    public void updateInventory(OrganizationOuterClass.Organization organization)
    {

    }

    @Override
    public Inventory.InventoryItem createInventoryItem(Inventory.InventoryItem inventoryItem)
    {
        return null;
    }

    @Override
    public void updateInventoryItem(Inventory.InventoryItem inventoryItem)
    {

    }

    @Override
    public void applyInventoryOperation(Inventory.InventoryOperation inventoryOperation)
    {

    }

    @Override
    public void startInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public void acceptInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public void rejectInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public Inventory.InventoryItemBrand createItemBrand(Inventory.InventoryItemBrand brand)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_BRANDS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(brand.getEntityId())))
                .with(QueryBuilder.put("brands", newId, brand.getBrandName()));
        _cassandraConnector.getSession().execute(query);

        return Inventory.InventoryItemBrand.newBuilder(brand)
                .setBrandId(newId.toString())
                .build();
    }

    @Override
    public void updateItemBrand(Inventory.InventoryItemBrand brand)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_BRANDS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(brand.getEntityId())))
                .with(QueryBuilder.put("brands", UUID.fromString(brand.getBrandId()), brand.getBrandName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getItemBrands(String entityId)
    {
        return getItems(entityId, INVENTORY_ITEM_BRANDS_TABLE, "brands");
    }

    @Override
    public Inventory.InventoryItemCategory createItemCategory(Inventory.InventoryItemCategory category)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_CATEGORIES_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(category.getEntityId())))
                .with(QueryBuilder.put("categories", newId, category.getCategoryName()));
        _cassandraConnector.getSession().execute(query);

        return Inventory.InventoryItemCategory.newBuilder(category)
                .setCategoryId(newId.toString())
                .build();
    }

    @Override
    public void updateItemCategory(Inventory.InventoryItemCategory category)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_CATEGORIES_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(category.getEntityId())))
                .with(QueryBuilder.put("categories", UUID.fromString(category.getCategoryId()), category.getCategoryName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getItemCategories(String entityId)
    {
        return getItems(entityId, INVENTORY_ITEM_CATEGORIES_TABLE, "categories");
    }

    @Override
    public Inventory.InventoryItemMeasurement createItemMeasurement(Inventory.InventoryItemMeasurement measurement)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_MEASUREMENTS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(measurement.getEntityId())))
                .with(QueryBuilder.put("measurements", newId, measurement.getMeasurementName()));
        _cassandraConnector.getSession().execute(query);

        return Inventory.InventoryItemMeasurement.newBuilder(measurement)
                .setMeasurementId(newId.toString())
                .build();
    }

    @Override
    public void updateItemMeasurement(Inventory.InventoryItemMeasurement measurement)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_MEASUREMENTS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(measurement.getEntityId())))
                .with(QueryBuilder.put("measurements", UUID.fromString(measurement.getMeasurementId()), measurement.getMeasurementName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getItemMeasurements(String entityId)
    {
        return getItems(entityId, INVENTORY_ITEM_MEASUREMENTS_TABLE, "measurements");
    }

    private Map<UUID, String> getItems(String entityId, String tableName, String columnName)
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, tableName)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(entityId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            return new HashMap<>();
        }

        return row.getMap(columnName, UUID.class, String.class);
    }
}
