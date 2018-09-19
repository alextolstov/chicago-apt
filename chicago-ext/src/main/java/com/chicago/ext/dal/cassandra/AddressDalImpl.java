package com.chicago.ext.dal.cassandra;

import com.chicago.dto.AddressOuterClass;
import com.chicago.ext.dal.AddressDal;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.ADDRESSES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_ID_TABLE;

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
                .value("office_number", address.getOfficeNumber())
                .value("apartment_number", address.getApartmentNumber())
                .value("city", address.getCity())
                .value("plase_name", address.getPlaceName())
                .value("county", address.getCounty())
                .value("zip_code", address.getZipCode())
                .value("country", address.getCountry());
        _cassandraConnector.getSession().execute(query);
        LOG.info("New user {} created");
        return newAddressId.toString();
    }

    @Override
    public void updateAddress(AddressOuterClass.Address address) throws Exception
    {

    }

    @Override
    public void deleteAddress(UUID address_id) throws Exception
    {

    }

    @Override
    public AddressOuterClass.Address getAddress(UUID address_id) throws Exception
    {
        return null;
    }
}
