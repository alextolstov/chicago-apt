package com.chicago.ext.dal.cassandra;

import com.chicago.dto.AddressOuterClass;
import com.chicago.ext.dal.AddressDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.ADDRESSES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;

public class AddressDalImpl implements AddressDal
{
    private static final Logger LOG = LoggerFactory.getLogger(AddressDalImpl.class);

    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public String createAddress(AddressOuterClass.Address address) throws Exception
    {
        UUID newAddressId = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, ADDRESSES_TABLE)
                .value("address_id", newAddressId)
                .value("street_name", address.getStreetName())
                .value("house_number", address.getHouseNumber())
                .value("office_apt_number", address.getOfficeAptNumber())
                .value("city", address.getCity())
                .value("place_name", address.getPlaceName())
                .value("county", address.getCounty())
                .value("zip_code", address.getZipCode())
                .value("country", address.getCountry());
        _cassandraConnector.getSession().execute(query);
        LOG.info("New address created");
        return newAddressId.toString();
    }

    @Override
    public void updateAddress(AddressOuterClass.Address address) throws Exception
    {
        Statement query = QueryBuilder.update(KEYSPACE, ADDRESSES_TABLE)
                .with(QueryBuilder.set("street_name", address.getStreetName()))
                .and(QueryBuilder.set("house_number", address.getHouseNumber()))
                .and(QueryBuilder.set("building_info", address.getBuildingInfo()))
                .and(QueryBuilder.set("office_apt_number", address.getOfficeAptNumber()))
                .and(QueryBuilder.set("city", address.getCity()))
                .and(QueryBuilder.set("place_name", address.getPlaceName()))
                .and(QueryBuilder.set("county", address.getCounty()))
                .and(QueryBuilder.set("state", address.getState()))
                .and(QueryBuilder.set("zip_code", address.getZipCode()))
                .and(QueryBuilder.set("country", address.getCountry()))
                .where(QueryBuilder.eq("address_id", UUID.fromString(address.getAddressId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public AddressOuterClass.Address getAddress(String addressId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ADDRESSES_TABLE)
                .where(QueryBuilder.eq("address_id", UUID.fromString(addressId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new AddressNotFoundException("No address with address_id " + addressId + " found");
        }

        // Populate protobuf
        return buildAddress(row);
    }

    private AddressOuterClass.Address buildAddress(Row row)
    {
        AddressOuterClass.Address.Builder builder = AddressOuterClass.Address.newBuilder();
        // Always non empty
        builder.setAddressId(row.getUUID("address_id").toString());
        // Could be empty
        if (row.getBytes("street_name") != null) builder.setStreetName(row.getString("street_name"));
        if (row.getString("house_number") != null) builder.setHouseNumber(row.getString("house_number"));
        if (row.getString("building_info") != null) builder.setBuildingInfo(row.getString("building_info"));
        if (row.getString("office_apt_number") != null) builder.setOfficeAptNumber(row.getString("office_apt_number"));
        if (row.getString("city") != null) builder.setCity(row.getString("city"));
        if (row.getString("place_name") != null) builder.setPlaceName(row.getString("place_name"));
        if (row.getString("county") != null) builder.setCounty(row.getString("county"));
        if (row.getString("state") != null) builder.setState(row.getString("state"));
        if (row.getString("zip_code") != null) builder.setZipCode(row.getString("zip_code"));
        if (row.getString("country") != null) builder.setCountry(row.getString("country"));

        return builder.build();
    }
}

