package com.chicago.ext.dal;

import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;

import java.util.List;

public interface SearchFiltersDal
{
    // Location
    String getCityId(String cityId) throws Exception;
    String getDistictId(String districtId) throws Exception;
    List<String> getSubwayStationsId(List<String> stationsId) throws Exception;

    // Main filter
    int getPropertyType(EnumTypes.PropertyType propertyType) throws Exception;
    int getMarket(EnumTypes.Market propertyMarket) throws Exception;
    // Additional filters
    int getViewFromWindow(EnumTypes.ViewFromWindow viewFromWindow) throws Exception;
    int getCeilingHeight(EnumTypes.CeilingHeight ceilingHeight) throws Exception;
    void addSearchFilter(String userId, String searchFilter) throws Exception;
}
