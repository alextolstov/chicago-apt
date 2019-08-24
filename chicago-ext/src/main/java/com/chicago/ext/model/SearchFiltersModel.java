package com.chicago.ext.model;

import lombok.Data;

import java.util.List;

public final class SearchFiltersModel
{
    public @Data static class SearchFilters
    {
        // Location
        private String cityId;
        private String districtId;
        private List<String> subwayStationsId;

        // Main filters
        private EnumTypes.PropertyType typeId;
        private EnumTypes.Market marketId;
        private List<Integer> roomsNumber;
        private int aptPriceFrom;
        private int aptPriceTo;
        private int aptSizeFrom;
        private int aptSizeTo;

        // Additional filters
        private EnumTypes.ViewFromWindow windowsView;
        private boolean balcony;
        private int kitchenSizeFrom;
        private int kitchenSizeTo;
        private EnumTypes.CeilingHeight ceilingHeight;

        // Floors
        int floorFrom;
        int floorTo;
        int floorsInHouseFrom;
        int floorsInHouseTo;
        boolean notFirstFloor;
        boolean notLastFloor;
        boolean lastFloor;
    }
}