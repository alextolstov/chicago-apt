package com.chicago.ext.model;

import lombok.Data;

import java.util.List;

public final class YandexSearchFiltersModel
{
    public @Data static class YandexSearchFilters
    {
        // Location
        private String cityId;
        private String districtId;
        private List<String> subwayStationsId;

        // Main filters
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