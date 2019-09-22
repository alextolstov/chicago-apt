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
        private String userId;
        private String cityId;
        private List<Integer> districtsList = new ArrayList();
        private List<Integer> subwayStationsList = new ArrayList();

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
                    .setUserId(model.userId)
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
            SearchFiltersModel.SearchFilters model = new SearchFilters();

            // Location
            model.setUserId(dto.getUserId());
            model.setCityId(dto.getCityId());
            model.setDistrictsList(dto.getDistrictIdList());
            model.setSubwayStationsList(dto.getSubwayStationIdList());

            // Main filters
            model.setMarketId(EnumTypes.Market.values()[dto.getMarketId().getNumber()]);
            model.setRoomsNumberList(dto.getRoomsNumberList());
            model.setAptPriceFrom(dto.getAptPriceFrom());
            model.setAptPriceTo(dto.getAptPriceTo());
            model.setAptSizeFrom(dto.getAptSizeFrom());
            model.setAptSizeTo(dto.getAptSizeTo());

            // Additional filters
            model.setWindowsView(EnumTypes.ViewFromWindow.values()[dto.getWindowsView().getNumber()]);
            model.setBalcony(dto.getBalcony());
            model.setKitchenSizeFrom(dto.getKitchenSizeFrom());
            model.setKitchenSizeTo(dto.getKitchenSizeTo());
            model.setCeilingHeight(EnumTypes.CeilingHeight.values()[dto.getCeilingHeight().getNumber()]);

            // Floors
            model.setFloorFrom(dto.getFloorFrom());
            model.setFloorTo(dto.getFloorTo());
            model.setFloorsInHouseFrom(dto.getFloorsInHouseFrom());
            model.setFloorsInHouseTo(dto.getFloorsInHouseTo());
            model.setNotFirstFloor(dto.getNotFirstFloor());
            model.setNotLastFloor(dto.getNotLastFloor());
            model.setLastFloor(dto.getLastFloor());

            return model;
        }
    }

}
