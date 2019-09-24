package com.chicago.ext.model;

import lombok.Data;

import java.util.List;

public final class CianSearchFiltersModel
{
    public @Data static class CianSearchFilters
    {
        // Location
        private String cityId;
        private List<Integer> districtsId;
        private List<Integer> subwayStationsId;

        // Main filters
        private EnumTypes.PropertyType typeId;
        private EnumTypes.Market marketId;
        private List<Integer> roomsNumber;
        private int aptPriceFrom;
        private int aptPriceTo;
        private int aptSizeFrom;
        private int aptSizeTo;

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
    }
}