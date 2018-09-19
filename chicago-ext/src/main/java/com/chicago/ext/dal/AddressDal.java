package com.chicago.ext.dal;

import com.chicago.dto.AddressOuterClass;

import java.util.UUID;

public interface AddressDal
{
    String createAddress(AddressOuterClass.Address address) throws Exception;

    void updateAddress(AddressOuterClass.Address address) throws Exception;

    AddressOuterClass.Address getAddress(String addressId) throws Exception;
}
