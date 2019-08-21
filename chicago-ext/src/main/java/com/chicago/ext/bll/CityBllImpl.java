package com.chicago.ext.bll;

import com.chicago.ext.dal.CityDal;
import com.chicago.ext.model.CityModel;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Service
public class CityBllImpl implements CityBll
{
    private static final Logger LOG = LoggerFactory.getLogger(CityBllImpl.class);
    @Inject
    private CityDal _cityDal;

    @Override
    public CityModel.City getCity(String cityId) throws Exception
    {
        return _cityDal.getCity(cityId);
    }
}
