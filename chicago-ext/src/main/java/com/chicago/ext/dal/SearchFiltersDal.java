package com.chicago.ext.dal;

import com.chicago.ext.model.SearchFiltersModel;

import java.util.List;

public interface SearchFiltersDal
{
    // Location
    String getCityId(String cityId) throws Exception;
    String getDistictId(String districtId) throws Exception;
    List<String> getSubwayStationsId(List<String> stationsId) throws Exception;

    // Main filter
    int getPropertyType(SearchFiltersModel.PropertyType propertyType) throws Exception;
    int getMarket(SearchFiltersModel.Market propertyMarket) throws Exception;
    // Additional filters
    int getViewFromWindow(SearchFiltersModel.ViewFromWindow viewFromWindow) throws Exception;
    int getCeilingHeight(SearchFiltersModel.CeilingHeight ceilingHeight) throws Exception;;
}
