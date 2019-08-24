package com.chicago.ext.dal.mysql;

import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.SearchFiltersModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;

public class SearchFiltersDalImpl implements SearchFiltersDal
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchFiltersDalImpl.class);

    @Inject
    private DbConnector<Connection> _mySqlConnector;


    @Override
    public String getCityId(String cityId) throws Exception
    {
        return null;
    }

    @Override
    public String getDistictId(String districtId) throws Exception
    {
        return null;
    }

    @Override
    public List<String> getSubwayStationsId(List<String> stationsId) throws Exception
    {
        return null;
    }

    @Override
    public int getPropertyType(SearchFiltersModel.PropertyType propertyType) throws Exception
    {
        return 0;
    }

    @Override
    public int getMarket(SearchFiltersModel.Market propertyMarket) throws Exception
    {
        return 0;
    }

    @Override
    public int getViewFromWindow(SearchFiltersModel.ViewFromWindow viewFromWindow) throws Exception
    {
        return 0;
    }

    @Override
    public int getCeilingHeight(SearchFiltersModel.CeilingHeight ceilingHeight) throws Exception
    {
        return 0;
    }
}
