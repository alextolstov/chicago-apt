package com.chicago.ext.bll;

import com.chicago.dto.AddressOuterClass;
import com.chicago.ext.dal.AddressDal;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Service
public class AddressBllImpl implements AddressBll
{
    private static final Logger LOG = LoggerFactory.getLogger(AddressBllImpl.class);
    @Inject
    private AddressDal _addressDal;

    @Override
    public AddressOuterClass.Address createAddress(AddressOuterClass.Address address) throws Exception
    {
        String newAddressId = _addressDal.createAddress(address);
        return AddressOuterClass.Address.newBuilder(address)
                .setAddressId(newAddressId.toString())
                .build();
    }

    @Override
    public void updateAddress(AddressOuterClass.Address address) throws Exception
    {
        _addressDal.updateAddress(address);
    }

    @Override
    public AddressOuterClass.Address getAddress(String addressId) throws Exception
    {
        return _addressDal.getAddress(addressId);
    }
}
