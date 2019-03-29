package com.chicago.ext.dal.cassandra;

import com.chicago.dto.InventoryOuterClass;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.InventoryDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEMS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_BRANDS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_CATEGORIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_SUPPLIERS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_ITEM_UNITS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.INVENTORY_LOCATIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;

public class InventoryDalImpl implements InventoryDal
{
    private static final Logger LOG = LoggerFactory.getLogger(InventoryDalImpl.class);

    @Inject
    private DbConnector _cassandraConnector;

    @Override
    public String createInventory(InventoryOuterClass.Inventory inventory) throws Exception
    {
        UUID newInventory = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, INVENTORIES_TABLE)
                .value("organization_id", inventory.getOrganizationId())
                .value("inventory_id", newInventory)
                .value("inventory_name", inventory.getInventoryName())
                .value("description", inventory.getDescription());

        return newInventory.toString();
    }

    @Override
    public void updateInventory(InventoryOuterClass.Inventory inventory)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORIES_TABLE)
                .with(QueryBuilder.set("inventory_name", inventory.getInventoryName()))
                .and(QueryBuilder.set("description", inventory.getDescription()))
                .where(QueryBuilder.eq("organization_id", UUID.fromString(inventory.getOrganizationId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public InventoryOuterClass.Inventory getInventory(String organizationId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, INVENTORIES_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new Exception("No inventory for organization " + organizationId + " found");
        }
        return InventoryOuterClass.Inventory.newBuilder()
                .setOrganizationId(row.getUUID("organization_id").toString())
                .setInventoryId(row.getUUID("inventory_id").toString())
                .setInventoryName(row.getString("inventory_name"))
                .setDescription(row.getString("description"))
                .build();
    }

    @Override
    public String createInventoryItem(InventoryOuterClass.InventoryItem inventoryItem)
    {
        UUID newItemId = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, INVENTORY_ITEMS_TABLE)
                .value("entity_id", UUID.fromString(inventoryItem.getEntityId()))
                .value("inventory_id", UUID.fromString(inventoryItem.getInventoryId()))
                .value("item_id", newItemId)
                .value("item_category_id", UUID.fromString(inventoryItem.getItemCategoryId()))
                .value("item_brand_id", UUID.fromString(inventoryItem.getItemBrandId()))
                .value("item_unit_id", UUID.fromString(inventoryItem.getItemUnitId()))
                .value("item_supplier_id", UUID.fromString(inventoryItem.getItemSupplierId()))
                .value("description", inventoryItem.getDescription())
                .value("weight_net", inventoryItem.getWeightNet())
                .value("weight_gross", inventoryItem.getWeightGross())
                .value("package_weight", inventoryItem.getPackageWeight())
                .value("quantity_per_pack", inventoryItem.getQuantityPerPack())
                .value("inbound_quantity", inventoryItem.getInboundQuantity())
                .value("inbound_unit_id", UUID.fromString(inventoryItem.getInboundUnitId()))
                .value("outbound_quantity", inventoryItem.getOutboundQuantity())
                .value("outbound_unit_id", UUID.fromString(inventoryItem.getInboundUnitId()))
                .value("location_id", UUID.fromString(inventoryItem.getLocationId()))
                .value("ean13", inventoryItem.getEan13())
                .value("vendor_code", inventoryItem.getVendorCode())
                .value("discontinued", inventoryItem.getDiscontinued())
//                .value("image", inventoryItem.getImage())
                .value("certificate", inventoryItem.getCertificate())
                .value("notes", inventoryItem.getNotes())
                .value("vendor_price", inventoryItem.getVendorPrice())
                .value("retail_price", inventoryItem.getRetailPrice())
                .value("create_datetime", inventoryItem.getCreateDatetime());

        _cassandraConnector.getSession().execute(query);
        LOG.info("New item {} created", newItemId);
        return newItemId.toString();
    }

    @Override
    public void updateInventoryItem(InventoryOuterClass.InventoryItem inventoryItem)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEMS_TABLE)
                .with(QueryBuilder.set("item_category_id", UUID.fromString(inventoryItem.getItemCategoryId())))
                .and(QueryBuilder.set("item_category_id", UUID.fromString(inventoryItem.getItemCategoryId())))
                .and(QueryBuilder.set("item_brand_id", UUID.fromString(inventoryItem.getItemBrandId())))
                .and(QueryBuilder.set("item_unit_id", UUID.fromString(inventoryItem.getItemUnitId())))
                .and(QueryBuilder.set("item_supplier_id", UUID.fromString(inventoryItem.getItemSupplierId())))
                .and(QueryBuilder.set("description", inventoryItem.getDescription()))
                .and(QueryBuilder.set("weight_net", inventoryItem.getWeightNet()))
                .and(QueryBuilder.set("weight_gross", inventoryItem.getWeightGross()))
                .and(QueryBuilder.set("package_weight", inventoryItem.getPackageWeight()))
                .and(QueryBuilder.set("quantity_per_pack", inventoryItem.getQuantityPerPack()))
                .and(QueryBuilder.set("inbound_quantity", inventoryItem.getInboundQuantity()))
                .and(QueryBuilder.set("inbound_unit_id", UUID.fromString((inventoryItem.getInboundUnitId()))))
                .and(QueryBuilder.set("outbound_quantity", inventoryItem.getOutboundQuantity()))
                .and(QueryBuilder.set("outbound_unit_id", UUID.fromString(inventoryItem.getInboundUnitId())))
                .and(QueryBuilder.set("location_id", UUID.fromString(inventoryItem.getLocationId())))
                .and(QueryBuilder.set("ean13", inventoryItem.getEan13()))
                .and(QueryBuilder.set("vendor_code", inventoryItem.getVendorCode()))
                .and(QueryBuilder.set("discontinued", inventoryItem.getDiscontinued()))
                .and(QueryBuilder.set("image", inventoryItem.getImage()))
                .and(QueryBuilder.set("certificate", inventoryItem.getCertificate()))
                .and(QueryBuilder.set("notes", inventoryItem.getNotes()))
                .and(QueryBuilder.set("vendor_price", inventoryItem.getVendorPrice()))
                .and(QueryBuilder.set("retail_price", inventoryItem.getRetailPrice()))
                .where(QueryBuilder.eq("item_id", UUID.fromString(inventoryItem.getItemId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public InventoryOuterClass.InventoryItem getInventoryItem(String itemId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, INVENTORY_ITEMS_TABLE)
                .where(QueryBuilder.eq("item_id", UUID.fromString(itemId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new Exception("No item with " + itemId + " found");
        }

        return buildItem(row);
    }

    public List<InventoryOuterClass.InventoryItem> getInventoryItems(String entityId, String inventoryId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, INVENTORY_ITEMS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(entityId)))
                .and(QueryBuilder.eq("inventory_id", UUID.fromString(entityId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row;
        List<InventoryOuterClass.InventoryItem> items = new ArrayList<>();

        while ((row = result.one()) != null)
        {
            items.add(buildItem(row));
        }

        return items;
    }

    @Override
    public void applyInventoryOperation(InventoryOuterClass.InventoryOperation inventoryOperation)
    {

    }

    @Override
    public void startInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public void acceptInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public void rejectInventoryTransfer(InventoryOuterClass.InventoryTransfer inventoryTransfer)
    {

    }

    @Override
    public InventoryOuterClass.InventoryItemBrand createItemBrand(InventoryOuterClass.InventoryItemBrand brand)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_BRANDS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(brand.getEntityId())))
                .with(QueryBuilder.put("brands", newId, brand.getBrandName()));
        _cassandraConnector.getSession().execute(query);

        return InventoryOuterClass.InventoryItemBrand.newBuilder(brand)
                .setBrandId(newId.toString())
                .build();
    }

    @Override
    public void updateItemBrand(InventoryOuterClass.InventoryItemBrand brand)
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
    public InventoryOuterClass.InventoryItemCategory createItemCategory(InventoryOuterClass.InventoryItemCategory category)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_CATEGORIES_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(category.getEntityId())))
                .with(QueryBuilder.put("categories", newId, category.getCategoryName()));
        _cassandraConnector.getSession().execute(query);

        return InventoryOuterClass.InventoryItemCategory.newBuilder(category)
                .setCategoryId(newId.toString())
                .build();
    }

    @Override
    public void updateItemCategory(InventoryOuterClass.InventoryItemCategory category)
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
    public InventoryOuterClass.InventoryItemUnit createItemUnit(InventoryOuterClass.InventoryItemUnit unit)
    {
        UUID newId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_UNITS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(unit.getEntityId())))
                .with(QueryBuilder.put("units", newId, unit.getUnitName()));
        _cassandraConnector.getSession().execute(query);

        return InventoryOuterClass.InventoryItemUnit.newBuilder(unit)
                .setUnitId(newId.toString())
                .build();
    }

    @Override
    public void updateItemUnit(InventoryOuterClass.InventoryItemUnit unit)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_UNITS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(unit.getEntityId())))
                .with(QueryBuilder.put("units", UUID.fromString(unit.getUnitId()), unit.getUnitName()));

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
    public InventoryOuterClass.InventoryItemSupplier createItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier)
    {
        UUID newId = UUIDs.random();

        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_SUPPLIERS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(supplier.getEntityId())))
                .with(QueryBuilder.put("suppliers", newId, supplier.getSupplierName()));
        _cassandraConnector.getSession().execute(query);

        return InventoryOuterClass.InventoryItemSupplier.newBuilder(supplier)
                .setSupplierId(newId.toString())
                .build();
    }

    @Override
    public void updateItemSupplier(InventoryOuterClass.InventoryItemSupplier supplier)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_ITEM_SUPPLIERS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(supplier.getEntityId())))
                .with(QueryBuilder.put("suppliers", UUID.fromString(supplier.getSupplierId()), supplier.getSupplierName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getItemSuppliers(String entityId)
    {
        return getItems(entityId, INVENTORY_ITEM_SUPPLIERS_TABLE, "suppliers");
    }

    private InventoryOuterClass.InventoryItem buildItem(Row row) throws Exception
    {
        InventoryOuterClass.InventoryItem.Builder builder = InventoryOuterClass.InventoryItem.newBuilder();
        builder.setEntityId(row.getUUID("entity_id").toString());
        builder.setItemId(row.getUUID("item_id").toString());
        if (row.getUUID("item_category_id") != null)
            builder.setItemCategoryId(row.getUUID("item_category_id").toString());
        if (row.getUUID("item_brand_id") != null)
            builder.setItemBrandId(row.getUUID("item_brand_id").toString());
        if (row.getUUID("item_unit_id") != null)
            builder.setItemUnitId(row.getUUID("item_unit_id").toString());
        if (row.getUUID("item_supplier_id") != null)
            builder.setItemSupplierId(row.getUUID("item_supplier_id").toString());
        if (row.getString("description") != null)
            builder.setDescription(row.getString("description"));
        builder.setWeightNet(row.getInt("weight_net"));
        builder.setWeightGross(row.getInt("weight_gross"));
        builder.setPackageWeight(row.getInt("package_weight"));
        builder.setQuantityPerPack(row.getInt("quantity_per_pack"));
        builder.setInboundQuantity(row.getInt("inbound_quantity"));
        if (row.getUUID("inbound_unit_id") != null)
            builder.setInboundUnitId(row.getUUID("inbound_unit_id").toString());
        builder.setOutboundQuantity(row.getInt("outbound_quantity"));
        if (row.getUUID("outbound_unit_id") != null)
            builder.setOutboundUnitId(row.getUUID("outbound_unit_id").toString());
        if (row.getUUID("location_id") != null)
            builder.setLocationId(row.getUUID("location_id").toString());
        if (row.getString("ean13") != null)
            builder.setEan13(row.getString("ean13"));
        if (row.getString("vendor_code") != null)
            builder.setVendorCode(row.getString("vendor_code"));
        builder.setDiscontinued(row.getBool("discontinued"));
        if (row.getBytes("image") != null)
            builder.setImage(ByteString.copyFrom(row.getBytes("image").array()));
        if (row.getString("certificate") != null)
            builder.setCertificate(row.getString("certificate"));
        if (row.getString("notes") != null)
            builder.setNotes(row.getString("notes"));
        builder.setVendorPrice(row.getFloat("vendor_price"));
        builder.setRetailPrice(row.getFloat("retail_price"));

        return builder.build();
    }

    @Override
    public InventoryOuterClass.InventoryLocation createInventoryLocation(InventoryOuterClass.InventoryLocation location)
    {
        UUID newId = UUIDs.random();

        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_LOCATIONS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(location.getEntityId())))
                .with(QueryBuilder.put("locations", newId, location.getLocationName()));
        _cassandraConnector.getSession().execute(query);

        return InventoryOuterClass.InventoryLocation.newBuilder(location)
                .setLocationId(newId.toString())
                .build();
    }

    @Override
    public void updateInventoryLocation(InventoryOuterClass.InventoryLocation location)
    {
        Statement query = QueryBuilder.update(KEYSPACE, INVENTORY_LOCATIONS_TABLE)
                .where(QueryBuilder.eq("entity_id", UUID.fromString(location.getEntityId())))
                .with(QueryBuilder.put("locations", UUID.fromString(location.getLocationId()), location.getLocationName()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getInventoryLocations(String entityId)
    {
        return getItems(entityId, INVENTORY_LOCATIONS_TABLE, "locations");
    }
}
