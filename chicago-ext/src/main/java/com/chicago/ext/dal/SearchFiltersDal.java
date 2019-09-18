package com.chicago.ext.dal;

import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;

import java.util.List;

public interface SearchFiltersDal
{
    // Location
    String getCityId(String cityId) throws Exception;
    List<String> getDistrictsList(List<String> districtsList) throws Exception;
    List<String> getSubwayStationsList(List<String> stationsList) throws Exception;
    List<Integer> getRoomsNumberList(List<Integer> roomsNumberList) throws Exception;
    int getPriceFrom(int price) throws Exception;
    int getPriceTo(int price) throws Exception;
    int getAptSizeFrom(int size) throws Exception;
    int getAptSizeTo(int size) throws Exception;
    int getKitchenSizeFrom(int size) throws Exception;
    int getKitchenSizeTo(int size) throws Exception;
    int getFloorFrom(int floor) throws Exception;
    int getFloorTo(int floor) throws Exception;
    int getFloorsInHouseFrom(int floor) throws Exception;
    int getFloorsInHouseTo(int floor) throws Exception;
    boolean isLastFloor(boolean isLastFloor) throws Exception;
    boolean isNotFirstFloor(boolean isNotFirstFloor) throws Exception;
    boolean isNotLastFloor(boolean isNotLastFloor) throws Exception;

    // Main filter
    int getPropertyType(EnumTypes.PropertyType propertyType) throws Exception;
    int getMarket(EnumTypes.Market propertyMarket) throws Exception;
    // Additional filters
    int getViewFromWindow(EnumTypes.ViewFromWindow viewFromWindow) throws Exception;
    int getCeilingHeight(EnumTypes.CeilingHeight ceilingHeight) throws Exception;
    void addSearchFilter(String userId, String searchFilter) throws Exception;
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