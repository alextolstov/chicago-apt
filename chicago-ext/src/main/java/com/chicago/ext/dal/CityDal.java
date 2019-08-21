package com.chicago.ext.dal;

import com.chicago.ext.model.CityModel;

public interface CityDal
{
    CityModel.City getCity(String cityId) throws Exception;
}
