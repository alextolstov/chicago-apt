package com.chicago.ext.bll;

import com.chicago.dto.AddressOuterClass;
import com.chicago.ext.model.CityModel;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface CityBll
{
    CityModel.City getCity(String cityId) throws Exception;
}


