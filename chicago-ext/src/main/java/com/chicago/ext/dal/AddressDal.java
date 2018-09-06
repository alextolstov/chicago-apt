package com.chicago.ext.dal;

import com.chicago.dto.AddressOuterClass;

import java.util.UUID;

public interface AddressDal
{
    void createAddress(AddressOuterClass.Address address) throws Exception;

    void updateAddress(AddressOuterClass.Address address) throws Exception;

    void deleteAddress(UUID address_id) throws Exception;

    AddressOuterClass.Address getAddress(UUID address_id) throws Exception;
}
