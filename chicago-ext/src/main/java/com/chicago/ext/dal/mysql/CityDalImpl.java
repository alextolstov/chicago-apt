package com.chicago.ext.dal.mysql;

import com.chicago.ext.dal.CityDal;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.model.CityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.UUID;

public class CityDalImpl implements CityDal
{
    private static final Logger LOG = LoggerFactory.getLogger(CityDalImpl.class);

    @Inject
    private DbConnector<Connection> _mySqlConnector;


    @Override
    public CityModel.City getCity(String cityId) throws Exception
    {
        // Distcircts
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCityDistrict(?) }");
        CityModel.City cmodel = new CityModel.City();
        cmodel.setCityId(cityId);

        stmt.setString("cityId", cityId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next())
        {
            CityModel.District d = new CityModel.District();
            d.setDistrictId(rs.getInt("DistrictId"));
            d.setDistrictName(rs.getString("DistrictName"));
            d.setDistrictClassifier(rs.getString("ClassifierName"));
            cmodel.getDistricts().add(d);
        }
        stmt.close();

        // Subway
        stmt = _mySqlConnector.getSession().prepareCall("{ call spGetCitySubwayLine(?) }");
        stmt.setString("cityId", cityId);
        rs = stmt.executeQuery();

        while (rs.next())
        {
            CityModel.SubwayLine l = new CityModel.SubwayLine();
            l.setLineId(rs.getString("LineId"));
            l.setLineName(rs.getString("LineName"));
            CallableStatement stationStmt = _mySqlConnector.getSession().prepareCall("{ call spGetCitySubwayStation(?, ?) }");
            stationStmt.setString("cityId", cityId);
            stationStmt.setString("lineId", l.getLineId());
            ResultSet stationRs = stationStmt.executeQuery();
            while (stationRs.next())
            {
                CityModel.SubwayStation station = new CityModel.SubwayStation();
                station.setStationId(stationRs.getInt("StationId"));
                station.setStationName(stationRs.getString("StationName"));
                l.getSubwayStations().add(station);
            }
            stationStmt.close();

            cmodel.getSubwayLines().add(l);
        }
        stmt.close();
        return cmodel;
    }
}
