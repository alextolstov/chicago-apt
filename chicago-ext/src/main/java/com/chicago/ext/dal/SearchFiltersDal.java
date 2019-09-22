package com.chicago.ext.dal;

import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;

import java.util.List;

public interface SearchFiltersDal
{
    // Location
    String getCityId(String cityId) throws Exception;
    List<String> getDistrictsList(List<Integer> districtsList) throws Exception;
    List<String> getSubwayStationsList(List<Integer> stationsList) throws Exception;
    String getPriceFrom(int price) throws Exception;
    String getPriceTo(int price) throws Exception;
    String getAptSizeFrom(int size) throws Exception;
    String getAptSizeTo(int size) throws Exception;

    // Main filter
    int getPropertyType(EnumTypes.PropertyType propertyType) throws Exception;
    int getMarket(EnumTypes.Market propertyMarket) throws Exception;
    // Additional filters
    int getViewFromWindow(EnumTypes.ViewFromWindow viewFromWindow) throws Exception;
    int getCeilingHeight(EnumTypes.CeilingHeight ceilingHeight) throws Exception;
    void addSearchFilter(String userId, String searchFilter) throws Exception;
    List<String> getSearchFilters(String userId) throws Exception;
}
/*

    // Additional filters
    private boolean balcony;
    private int kitchenSizeFrom;
    private int kitchenSizeTo;

    // Floors
    int floorFrom;
    int floorTo;
    int floorsInHouseFrom;
    int floorsInHouseTo;
    boolean notFirstFloor;
    boolean notLastFloor;
    boolean lastFloor;
    */
