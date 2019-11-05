package com.chicago.ext.model;

import com.chicago.dto.Searchfilters;
import lombok.Data;

import java.util.List;

public final class PropertyModel
{
    public @Data static class Property
    {
        private String propertyId;
        private String cityName;
        private String streetName;
        private String buildingNumber;

        private byte[] floorPlan;
        private EnumTypes.PropertyType typeId;

        private int aptPrice;
        private float aptSize;

        private EnumTypes.ViewFromWindow windowsView;
        private Boolean balcony;
        private float kitchenSize;
        private float ceilingHeight;
        private int floor;
        private int floorsInHouse;
        private int roomsNumber;

    }

    public @Data static class PropertyExtraInfo
    {
        private String linkToOriginalAd;
        private String linkToHouseInfo;
        private String linkToHistoricalInfo;
        private String linkToGoogleMap; //
        // Construction
        private EnumTypes.FloorBeamsMaterial floorBeamsMaterial;
        // Extra, depreciation
        private float floorDepreciationPercent;
        private float sewerDepreciationPercent;
        private float wallsDepreciationPercent;
        // Other
        private int complainsNumber;
    }

    public static class PropertyConvertor
    {
        public Searchfilters.Property toDto(PropertyModel.Property propertyModel)
        {
            return null;
        }
    }
}