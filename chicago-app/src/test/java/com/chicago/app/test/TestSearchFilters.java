package com.chicago.app.test;

import com.chicago.dto.Searchfilters;
import com.chicago.ext.bll.SearchFiltersBll;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.SearchFiltersModel;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TestSearchFilters
{
    @Test
    public void testReadCity() {
        Searchfilters.SearchFilters searchFiltersDto = Searchfilters.SearchFilters.newBuilder()
                .addDistrictId("17")
                .addDistrictId("11")
                .addSubwayStationId("4")
                .addSubwayStationId("19")
                .addSubwayStationId("56")
                .setCityId("c2deb16a-0330-4f05-821f-1d09c93331e6")
                .setAptPriceFrom(5000000)
                .setAptPriceTo(15000000)
                .setAptSizeFrom(60)
                .setAptSizeTo(120)
                .setBalcony(true)
                .setKitchenSizeFrom(6)
                .setKitchenSizeTo(15)
                .setLastFloor(false)
                .setNotFirstFloor(false)
                .setNotLastFloor(true)
                .setFloorFrom(3)
                .setFloorTo(5)
                .setFloorsInHouseFrom(1)
                .setFloorsInHouseTo(10)
                .build();

        SearchFiltersModel.SearchFiltersConvertor cnv = new SearchFiltersModel.SearchFiltersConvertor();
        SearchFiltersModel.SearchFilters model = cnv.fromDto(searchFiltersDto);
        Assert.assertEquals(model.getCityId(), "c2deb16a-0330-4f05-821f-1d09c93331e6");
        Assert.assertEquals(model.getAptSizeFrom(), 60);
        Assert.assertEquals(model.getAptSizeTo(), 120);
        Assert.assertEquals(model.isBalcony(), true);

        // Now test DAL
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
        TestMySqlConnector ts = new TestMySqlConnector();
        ServiceLocatorUtilities.bind(serviceLocator, new TestMySqlBinder(ts));

        DbConnector<Connection> dbService = ts;

        SearchFiltersDal searchFiltersDal = serviceLocator.getService(SearchFiltersDal.class);

        try
        {
            String cityId;
            List<String> districts = new ArrayList();
            List<String> stations = new ArrayList();
            cityId = searchFiltersDal.getCityId(model.getCityId());
            districts = searchFiltersDal.getDistrictsList(model.getDistrictsList());
            stations = searchFiltersDal.getSubwayStationsList(model.getSubwayStationsList());

            String str1 = URLEncoder.encode("[", "UTF-8");
            String str2 = URLEncoder.encode("]", "UTF-8");
            String httpRequest = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=flat&region=";
            httpRequest +=  searchFiltersDal.getCityId(model.getCityId());
            int idx = 0;
            for (String station : stations)
            {
                httpRequest += "&metro" + str1 + Integer.toString(idx++) + str2 +  "=" + station;
            }
            idx = 0;
            for (String district : districts)
            {
                httpRequest += "&district" + str1 + Integer.toString(idx++) + str2 +  "=" + district;
            }
            if ( model.getAptPriceFrom()!=0 )
            {
                httpRequest += "&minprice="+ searchFiltersDal.getPriceFrom(model.getAptPriceFrom());
            }
            if ( model.getAptPriceTo()!=0 )
            {
                httpRequest += "&maxprice="+ searchFiltersDal.getPriceTo(model.getAptPriceTo());
            }
            if ( model.getAptSizeFrom() !=0 )
            {
                httpRequest += "&mintarea="+ searchFiltersDal.getPriceFrom(model.getAptSizeFrom());
            }
            if ( model.getAptSizeTo() !=0 )
            {
                httpRequest += "&maxtarea="+ searchFiltersDal.getPriceTo(model.getAptSizeTo());
            }
                int i=1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        SearchFiltersBll searchFiltersBll = serviceLocator.getService(SearchFiltersBll.class);
//        String abc = searchFiltersBll.getCianSearchFilters();
  //      Assert.assertEquals("test string", abc);
    }
}
/*
тест провека
 */
