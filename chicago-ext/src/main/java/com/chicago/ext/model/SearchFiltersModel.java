package com.chicago.ext.model;

import com.chicago.dto.Searchfilters;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

public final class SearchFiltersModel
{
//    @Builder
    public @Data static class SearchFilters
    {
        // Location
        private String cityId;
        private List<String> districtId = new ArrayList();
        private List<String> subwayStationsId = new ArrayList();

        // Main filters
        private EnumTypes.PropertyType typeId;
        private EnumTypes.Market marketId;
        private List<Integer> roomsNumber = new ArrayList();
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

    public static class SearchFiltersConvertor
    {
        public SearchFiltersModel.SearchFilters fromDto(Searchfilters.SearchFilters searchFiltersDto)
        {
            SearchFiltersModel.SearchFilters filtersModel = new SearchFilters();
            filtersModel.setCityId(searchFiltersDto.getCityId());
            filtersModel.setDistrictId(searchFiltersDto.getDistrictIdList());
            filtersModel.setSubwayStationsId(searchFiltersDto.getSubwayStationIdList());

            return filtersModel;
        }
    }

}