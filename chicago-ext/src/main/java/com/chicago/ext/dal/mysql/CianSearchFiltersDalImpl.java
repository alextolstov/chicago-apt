package com.chicago.ext.dal.mysql;

import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.EnumTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CianSearchFiltersDalImpl extends AbstractSearchFiltersDal
{
    private static final Logger LOG = LoggerFactory.getLogger(CianSearchFiltersDalImpl.class);
    private int _client = 1;
    private HashMap<Integer, String> _mapDistricts = new HashMap<>();
    private HashMap<Integer, String> _mapStations = new HashMap<>();

    @Inject
    private DbConnector<Connection> _mySqlConnector;

    @Override
    public String getCityId(String cityId) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetWebsiteCity(?,?) }");
        stmt.setInt("websiteId", _client);
        stmt.setString("cityId", cityId);
        ResultSet cityRs = stmt.executeQuery();
        String cianCity = null;

        while (cityRs.next())
        {
            cianCity = cityRs.getString("WebsiteCityId");
        }
        stmt.close();
        return cianCity;
    }

    @Override
    public List<String> getDistrictsList(List<Integer> districtsList) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetWebsiteDistrict(?) }");
        stmt.setInt("websiteId", _client);
        ResultSet districtRs = stmt.executeQuery();
        while (districtRs.next())
        {
            _mapDistricts.put(districtRs.getInt("DistrictId"),districtRs.getString("WebsiteDistrictId"));
        }
        stmt.close();

        List<String> cianDistrictsList = new ArrayList<>();
        for (Integer district : districtsList)
        {
            cianDistrictsList.add(_mapDistricts.get(district));
        }
        return cianDistrictsList;
    }

    @Override
    public List<String> getSubwayStationsList(List<Integer> stationsList) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetWebsiteStation(?) }");
        stmt.setInt("websiteId", _client);
        ResultSet stationRs = stmt.executeQuery();
        while (stationRs.next())
        {
            _mapStations.put(stationRs.getInt("StationId"),stationRs.getString("WebsiteStationId"));
        }
        stmt.close();

        List<String> cianStationsList = new ArrayList<>();
        for (int station : stationsList)
        {
            cianStationsList.add(_mapStations.get(station));
        }
        return cianStationsList;

    }

    @Override
    public int getPropertyType(EnumTypes.PropertyType propertyType) throws Exception
    {
        return propertyType.getValue();
    }

    @Override
    public int getMarket(EnumTypes.Market propertyMarket) throws Exception
    {
        return propertyMarket.getValue();
    }

    @Override
    public int getViewFromWindow(EnumTypes.ViewFromWindow viewFromWindow) throws Exception
    {
        return 0;
    }

    @Override
    public int getCeilingHeight(EnumTypes.CeilingHeight ceilingHeight) throws Exception
    {
        return 0;
    }

    @Override
    public int getAptPriceFrom(int price) throws Exception
    {
        return price;
    }

    @Override
    public int getAptPriceTo(int price) throws Exception
    {
        return price;
    }

    @Override
    public int getAptSizeFrom(int size) throws Exception
    {
        return size;
    }

    @Override
    public int getAptSizeTo(int size) throws Exception
    {
        return size;
    }

    @Override
    public List<Integer> getRoomsNumberList(List<Integer> roomsNumberList) throws Exception
    {
        return roomsNumberList;
    }

    @Override
    public boolean isLastFloor(boolean isLastFloor) throws Exception
    {
        return isLastFloor;
    }

    @Override
    public boolean isNotFirstFloor(boolean isNotFirstFloor) throws Exception
    {
        return isNotFirstFloor;
    }

    @Override
    public boolean isNotLastFloor(boolean isNotLastFloor) throws Exception
    {
        return isNotLastFloor;
    }

    @Override
    public int getFloorFrom(int floor) throws Exception
    {
        return floor;
    }

    @Override
    public int getFloorTo(int floor) throws Exception
    {
        return floor;
    }

    @Override
    public int getFloorsInHouseFrom(int floor) throws Exception
    {
        return floor;
    }

    @Override
    public int getFloorsInHouseTo(int floor) throws Exception
    {
        return floor;
    }

    @Override
    public int getKitchenSizeFrom(int size) throws Exception
    {
        return size;
    }

    @Override
    public int getKitchenSizeTo(int size) throws Exception
    {
        return size;
    }
}
