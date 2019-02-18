package com.chicago.ext.dal.cassandra;

import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.ext.dal.DbConnector;
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
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_UNITS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;

public class InventoryDalImpl implements InventoryDal
{
    private static final Logger LOG = LoggerFactory.getLogger(InventoryDalImpl.class);

    @Inject
    private DbConnector _cassandraConnector;

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
    public Inventory.InventoryItemUnit createItemUnit(Inventory.InventoryItemUnit unit)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_UNITS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(unit.getEntityId())))
                .with(QueryBuilder.put("measurements", newId, unit.getUnitName()));
        _cassandraConnector.getSession().execute(query);

        return Inventory.InventoryItemUnit.newBuilder(unit)
                .setUnitId(newId.toString())
                .build();
    }

    @Override
    public void updateItemUnit(Inventory.InventoryItemUnit unit)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_UNITS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(unit.getEntityId())))
                .with(QueryBuilder.put("unit", UUID.fromString(unit.getUnitId()), unit.getUnitName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getItemUnits(String entityId)
    {
        return getItems(entityId, INVENTORY_ITEM_UNITS_TABLE, "units");
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

    @Override
    public Inventory.InventoryItemSupplier createItemUnitSupplier(Inventory.InventoryItemSupplier supplier)
    {
        return null;
    }

    @Override
    public void updateItemSupplier(Inventory.InventoryItemSupplier supplier)
    {

    }

    @Override
    public Map<UUID, String> getItemSuppliers(String entityId)
    {
        return null;
    }
}
