package com.chicago.ext.model;

import com.chicago.dto.Searchfilters;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public final class SearchFiltersModel
{
    public @Data static class SearchFilters
    {
        // Location
        private String cityId;
        private List<String> districtsList = new ArrayList();
        private List<String> subwayStationsList = new ArrayList();

        // Main filters
        private EnumTypes.PropertyType typeId;
        private EnumTypes.Market marketId;
        private List<Integer> roomsNumberList = new ArrayList();
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

            // Location
            filtersModel.setCityId(searchFiltersDto.getCityId());
            filtersModel.setDistrictsList(searchFiltersDto.getDistrictIdList());
            filtersModel.setSubwayStationsList(searchFiltersDto.getSubwayStationIdList());
            filtersModel.setTypeId(EnumTypes.PropertyType.values()[searchFiltersDto.getTypeId().getNumber()]);

            // Main filters
            filtersModel.setMarketId(EnumTypes.Market.values()[searchFiltersDto.getMarketId().getNumber()]);
            filtersModel.setRoomsNumberList(searchFiltersDto.getRoomsNumberList());
            filtersModel.setAptPriceFrom(searchFiltersDto.getAptPriceFrom());
            filtersModel.setAptPriceTo(searchFiltersDto.getAptPriceTo());
            filtersModel.setAptSizeFrom(searchFiltersDto.getAptSizeFrom());
            filtersModel.setAptSizeTo(searchFiltersDto.getAptSizeTo());

            // Additional filters
            filtersModel.setWindowsView(EnumTypes.ViewFromWindow.values()[searchFiltersDto.getWindowsView().getNumber()]);
            filtersModel.setBalcony(searchFiltersDto.getBalcony());
            filtersModel.setKitchenSizeFrom(searchFiltersDto.getKitchenSizeFrom());
            filtersModel.setKitchenSizeTo(searchFiltersDto.getKitchenSizeTo());
            filtersModel.setCeilingHeight(EnumTypes.CeilingHeight.values()[searchFiltersDto.getCeilingHeight().getNumber()]);

            // Floors
            filtersModel.setFloorFrom(searchFiltersDto.getFloorFrom());
            filtersModel.setFloorTo(searchFiltersDto.getFloorTo());
            filtersModel.setFloorsInHouseFrom(searchFiltersDto.getFloorsInHouseFrom());
            filtersModel.setFloorsInHouseTo(searchFiltersDto.getFloorsInHouseTo());
            filtersModel.setNotFirstFloor(searchFiltersDto.getNotFirstFloor());
            filtersModel.setNotLastFloor(searchFiltersDto.getNotLastFloor());
            filtersModel.setLastFloor(searchFiltersDto.getLastFloor());

            return filtersModel;
        }
    }

}
