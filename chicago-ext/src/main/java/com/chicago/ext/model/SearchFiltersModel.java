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
        private int floorFrom;
        private int floorTo;
        private int floorsInHouseFrom;
        private int floorsInHouseTo;

        private boolean notFirstFloor;
        private boolean notLastFloor;
        private boolean lastFloor;
    }

    public static class SearchFiltersConvertor
    {
        public Searchfilters.SearchFilters toDto(SearchFiltersModel.SearchFilters model)
        {
            Searchfilters.SearchFilters dto = Searchfilters.SearchFilters.newBuilder()
                    .setCityId(model.cityId)
                    .addAllDistrictId(model.getDistrictsList())
                    .addAllSubwayStationId(model.getSubwayStationsList())
                    .setTypeId(Searchfilters.PropertyType.values()[model.getTypeId().getValue()])
                    .setMarketId(Searchfilters.Market.values()[model.getMarketId().getValue()])
                    .addAllRoomsNumber(model.getRoomsNumberList())
                    .setAptPriceFrom(model.getAptPriceFrom())
                    .setAptPriceTo(model.getAptPriceTo())
                    .setAptSizeFrom(model.getAptSizeFrom())
                    .setAptSizeTo(model.getAptSizeTo())
                    .setWindowsView(Searchfilters.ViewFromWindow.values()[model.getWindowsView().getValue()])
                    .setBalcony(model.isBalcony())
                    .setKitchenSizeFrom(model.getKitchenSizeFrom())
                    .setKitchenSizeTo(model.getKitchenSizeTo())
                    .setCeilingHeight(Searchfilters.CeilingHeight.values()[model.getCeilingHeight().getValue()])
                    .setFloorFrom(model.getFloorFrom())
                    .setFloorTo(model.getFloorTo())
                    .setFloorsInHouseFrom(model.getFloorsInHouseFrom())
                    .setFloorsInHouseTo(model.getFloorsInHouseTo())
                    .setNotFirstFloor(model.isNotFirstFloor())
                    .setNotLastFloor(model.isNotLastFloor())
                    .setLastFloor(model.isLastFloor())
                    .build();
            return dto;
        }

        public SearchFiltersModel.SearchFilters fromDto(Searchfilters.SearchFilters dto)
        {
            SearchFiltersModel.SearchFilters filtersModel = new SearchFilters();

            // Location
            filtersModel.setCityId(dto.getCityId());
            filtersModel.setDistrictsList(dto.getDistrictIdList());
            filtersModel.setSubwayStationsList(dto.getSubwayStationIdList());

            // Main filters
            filtersModel.setMarketId(EnumTypes.Market.values()[dto.getMarketId().getNumber()]);
            filtersModel.setRoomsNumberList(dto.getRoomsNumberList());
            filtersModel.setAptPriceFrom(dto.getAptPriceFrom());
            filtersModel.setAptPriceTo(dto.getAptPriceTo());
            filtersModel.setAptSizeFrom(dto.getAptSizeFrom());
            filtersModel.setAptSizeTo(dto.getAptSizeTo());

            // Additional filters
            filtersModel.setWindowsView(EnumTypes.ViewFromWindow.values()[dto.getWindowsView().getNumber()]);
            filtersModel.setBalcony(dto.getBalcony());
            filtersModel.setKitchenSizeFrom(dto.getKitchenSizeFrom());
            filtersModel.setKitchenSizeTo(dto.getKitchenSizeTo());
            filtersModel.setCeilingHeight(EnumTypes.CeilingHeight.values()[dto.getCeilingHeight().getNumber()]);

            // Floors
            filtersModel.setFloorFrom(dto.getFloorFrom());
            filtersModel.setFloorTo(dto.getFloorTo());
            filtersModel.setFloorsInHouseFrom(dto.getFloorsInHouseFrom());
            filtersModel.setFloorsInHouseTo(dto.getFloorsInHouseTo());
            filtersModel.setNotFirstFloor(dto.getNotFirstFloor());
            filtersModel.setNotLastFloor(dto.getNotLastFloor());
            filtersModel.setLastFloor(dto.getLastFloor());

            return filtersModel;
        }
    }

}
