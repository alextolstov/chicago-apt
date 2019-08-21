package com.chicago.app.test;

import com.chicago.dto.CityOuterClass;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.model.CityModel;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class TestMySqlDal
{
    @Test
    public void testReadCity()
    {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
        TestMySqlConnector ts = new TestMySqlConnector();
        ServiceLocatorUtilities.bind(serviceLocator, new TestMySqlBinder(ts));

        DbConnector<Connection> dbService = ts;
        try
        {
            // Distcircts
            CallableStatement stmt = dbService.getSession().prepareCall("{ call spGetCityDistrict(?) }");
            String cityId = "c2deb16a-0330-4f05-821f-1d09c93331e6";
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
            stmt = dbService.getSession().prepareCall("{ call spGetCitySubwayLine(?) }");
            stmt.setString("cityId", cityId);
            rs = stmt.executeQuery();
            while (rs.next())
            {
                CityModel.SubwayLine l = new CityModel.SubwayLine();
                l.setLineId(rs.getString("LineId"));
                l.setLineName(rs.getString("LineName"));
                CallableStatement stationStmt = dbService.getSession().prepareCall("{ call spGetCitySubwayStation(?, ?) }");
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
            CityOuterClass.City dto = new CityModel.CityConvertor().toDto(cmodel);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
