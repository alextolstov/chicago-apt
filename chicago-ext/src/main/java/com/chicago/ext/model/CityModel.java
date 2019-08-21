package com.chicago.ext.model;

import lombok.Data;
import com.chicago.dto.CityOuterClass;

import java.util.ArrayList;
import java.util.List;

public final class CityModel
{
    public @Data static class District
    {
        private int districtId;
        private String districtName;
        private String districtClassifier;
    }

    public @Data static class SubwayLine
    {
        private String lineId;
        private String lineName;
        private List<SubwayStation> subwayStations = new ArrayList<>();
    }

    public @Data static class SubwayStation
    {
        private int stationId;
        private String stationName;
    }

    public @Data static class City
    {
        private String cityId;
        private List<District> districts = new ArrayList<>();
        private List<SubwayLine> subwayLines = new ArrayList<>();
    }

    public static class CityConvertor
    {
        public CityOuterClass.City toDto(City cityModel)
        {
            List<CityOuterClass.District> districts = new ArrayList<>();

            for (CityModel.District district : cityModel.getDistricts())
            {
                CityOuterClass.District districtDto = CityOuterClass.District.newBuilder()
                        .setDistrictId(district.getDistrictId())
                        .setDistrictName(district.getDistrictName())
                        .setDistrictClassifier(district.getDistrictClassifier())
                        .build();
                districts.add(districtDto);
            }

            List<CityOuterClass.SubwayLine> subwayLines = new ArrayList<>();

            for (CityModel.SubwayLine subwayLine : cityModel.getSubwayLines())
            {
                List<CityOuterClass.SubwayStation> stationsDto = new ArrayList<>();
                for (CityModel.SubwayStation subwayStation : subwayLine.getSubwayStations())
                {
                    CityOuterClass.SubwayStation stationDto = CityOuterClass.SubwayStation.newBuilder()
                            .setStationId(subwayStation.getStationId())
                            .setStationName(subwayStation.getStationName())
                            .build();
                    stationsDto.add(stationDto);
                }
                CityOuterClass.SubwayLine subwayLineDto = CityOuterClass.SubwayLine.newBuilder()
                        .setLineId(subwayLine.getLineId())
                        .setLineName(subwayLine.getLineName())
                        .addAllSubwayStations(stationsDto)
                        .build();
                subwayLines.add(subwayLineDto);
            }
            CityOuterClass.City cityDto = CityOuterClass.City.newBuilder()
                    .setCityId(cityModel.getCityId().toString())
                    .addAllDistricts(districts)
                    .addAllSubwayLines(subwayLines)
                    .build();

            return cityDto;
        }
    }
}