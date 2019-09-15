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

public class CianSearchFiltersDalImpl implements SearchFiltersDal
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
    public List<String> getDistrictsList(List<String> districtsList) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetHashWebsiteDistrict(?) }");
        stmt.setInt("websiteId", _client);
        ResultSet districtRs = stmt.executeQuery();
        while (districtRs.next())
        {
            _mapDistricts.put(districtRs.getInt("DistrictId"),districtRs.getString("WebsiteDistrictId"));
        }
        stmt.close();

        List<String> cianDistrictsList = new ArrayList<>();
        for (String district : districtsList)
        {
            cianDistrictsList.add(_mapDistricts.get(Integer.parseInt(district)));
        }
        return cianDistrictsList;
    }

    @Override
    public List<String> getSubwayStationsList(List<String> stationsList) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetHashWebsiteStation(?) }");
        stmt.setInt("websiteId", _client);
        ResultSet stationRs = stmt.executeQuery();
        while (stationRs.next())
        {
            _mapStations.put(stationRs.getInt("StationId"),stationRs.getString("WebsiteStationId"));
        }
        stmt.close();

        List<String> cianStationsList = new ArrayList<>();
        for (String station : stationsList)
        {
            cianStationsList.add(_mapStations.get(Integer.parseInt(station)));
        }
        return cianStationsList;

    }

    @Override
    public int getPropertyType(EnumTypes.PropertyType propertyType) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCityDistrict(?) }");
        return 0;
    }

    @Override
    public int getMarket(EnumTypes.Market propertyMarket) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCityDistrict(?) }");
        return 0;
    }

    @Override
    public int getViewFromWindow(EnumTypes.ViewFromWindow viewFromWindow) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCityDistrict(?) }");
        return 0;
    }

    @Override
    public int getCeilingHeight(EnumTypes.CeilingHeight ceilingHeight) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCityDistrict(?) }");
        return 0;
    }

    @Override
    public void addSearchFilter(String userId, String searchFilter) throws Exception
    {

    }

    public String getPriceFrom(int price) throws Exception
    {
        return Integer.toString(price);
    }
    public String getPriceTo(int price) throws Exception
    {
        return Integer.toString(price);
    }
    public String getAptSizeFrom(int size) throws Exception
    {
        return Integer.toString(size);
    }
    public String getAptSizeTo(int size) throws Exception
    {
        return Integer.toString(size);
    }
}
