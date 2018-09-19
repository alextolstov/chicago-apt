package com.chicago.ext.bll;

import com.chicago.dto.AddressOuterClass;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AddressBll
{
    AddressOuterClass.Address createAddress(AddressOuterClass.Address address) throws Exception;

    void updateAddress(AddressOuterClass.Address address) throws Exception;

    AddressOuterClass.Address getAddress(String addressId) throws Exception;
}


