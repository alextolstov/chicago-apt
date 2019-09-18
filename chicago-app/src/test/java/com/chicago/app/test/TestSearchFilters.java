package com.chicago.app.test;

import com.chicago.dto.Searchfilters;
import com.chicago.ext.bll.SearchFiltersBll;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.EnumTypes;
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
                //.addRoomsNumber(0)
                //.addRoomsNumber(2)
                //.addRoomsNumber(6)
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
                //.setLastFloor(true)
                //.setNotFirstFloor(true)
                //.setNotLastFloor(true)
                .setFloorFrom(2)
                .setFloorTo(5)
                .setKitchenSizeFrom(2)
                .setKitchenSizeTo(5)
                .setFloorsInHouseFrom(1)
                .setFloorsInHouseTo(16)
                .setTypeId(Searchfilters.PropertyType.ROOM)
                .setMarketId(Searchfilters.Market.FIRST)
                .build();

        SearchFiltersModel.SearchFiltersConvertor cnv = new SearchFiltersModel.SearchFiltersConvertor();
        SearchFiltersModel.SearchFilters model = cnv.fromDto(searchFiltersDto);
        Assert.assertEquals(model.getCityId(), "c2deb16a-0330-4f05-821f-1d09c93331e6");
        Assert.assertEquals(model.getAptSizeFrom(), 60);
        Assert.assertEquals(model.getAptSizeTo(), 120);
        Assert.assertTrue(model.isBalcony());
        //Assert.assertEquals(model.isLastFloor(), true);
        //Assert.assertEquals(model.isNotFirstFloor(), false);
        //Assert.assertEquals(model.isNotLastFloor(), true);
        Assert.assertEquals(model.getMarketId(), EnumTypes.Market.FIRST);
        Assert.assertEquals(model.getTypeId(), EnumTypes.PropertyType.ROOM);

        // Now test DAL
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
        TestMySqlConnector ts = new TestMySqlConnector();
        ServiceLocatorUtilities.bind(serviceLocator, new TestMySqlBinder(ts));

        DbConnector<Connection> dbService = ts;

        SearchFiltersDal searchFiltersDal = serviceLocator.getService(SearchFiltersDal.class);

        try
        {
            String cityId = searchFiltersDal.getCityId(model.getCityId());
            List<String> districts = searchFiltersDal.getDistrictsList(model.getDistrictsList());
            List<String> stations = searchFiltersDal.getSubwayStationsList(model.getSubwayStationsList());

            String str1 = URLEncoder.encode("[", "UTF-8");
            String str2 = URLEncoder.encode("]", "UTF-8");
            String httpRequest = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&region=";
            httpRequest +=  searchFiltersDal.getCityId(model.getCityId());


            if( model.getTypeId() == EnumTypes.PropertyType.APARTMENT )
            {
                httpRequest += "&offer_type=flat";
            }

            if( model.getTypeId() == EnumTypes.PropertyType.ROOM )
            {
                httpRequest += "&offer_type=flat&room0=1";
            }

            int idx = 0;
            for (String station : stations)
            {
                httpRequest += "&metro" + str1 + idx++ + str2 +  "=" + station;
            }

            idx = 0;
            for (String district : districts)
            {
                httpRequest += "&district" + str1 + idx++ + str2 +  "=" + district;
            }

            if ( model.getAptPriceFrom()!=0 )
            {
                httpRequest += "&minprice=" + searchFiltersDal.getAptPriceFrom(model.getAptPriceFrom());
            }

            if ( model.getAptPriceTo()!=0 )
            {
                httpRequest += "&maxprice=" + searchFiltersDal.getAptPriceTo(model.getAptPriceTo());
            }

            if ( model.getAptSizeFrom() !=0 )
            {
                httpRequest += "&mintarea=" + searchFiltersDal.getAptSizeFrom(model.getAptSizeFrom());
            }

            if ( model.getAptSizeTo() !=0 )
            {
                httpRequest += "&maxtarea=" + searchFiltersDal.getAptSizeTo(model.getAptSizeTo());
            }

            idx = 0;
            if ( model.getMarketId() == EnumTypes.Market.SECOND )
            {
                httpRequest += "&object_type" +str1 + "0" + str2 +  "=1";
            }

            if ( model.getMarketId() == EnumTypes.Market.FIRST )
            {
                httpRequest += "&object_type" +str1 + "0" + str2 +  "=2";
            }

            List<Integer> rooms = model.getRoomsNumberList();

            for (Integer room : rooms)
            {
                switch (room) {
                    case 0:
                        httpRequest += "&room9=1"; //&room0 just a room, not appartment
                        break;
                    case 1:
                        httpRequest += "&room1=1";
                        break;
                    case 2:
                        httpRequest += "&room2=1";
                        break;
                    case 3:
                        httpRequest += "&room3=1";
                        break;
                    case 4:
                        httpRequest += "&room4=1";
                        break;
                    case 5:
                        httpRequest += "&room5=1";
                        break;
                    case 6:
                        httpRequest += "&room6=1" ;
                        break;
                    default:
                        httpRequest += "&room9=0"; // appartment
                        break;
                }
            }

            if( model.isNotLastFloor())
            {
                httpRequest += "&floornl=1";
            }

            if(model.isLastFloor())
            {
                httpRequest += "&floornl=0";
            }

            if( model.isNotFirstFloor())
            {
                httpRequest += "&is_first_floor=0";
            }

            if ( model.getFloorFrom() !=0 )
            {
                httpRequest += "&minfloor="+ Integer.toString(searchFiltersDal.getFloorFrom(model.getFloorFrom()));
            }

            if ( model.getFloorTo() !=0 )
            {
                httpRequest += "&maxfloor="+ Integer.toString(searchFiltersDal.getFloorTo(model.getFloorTo()));
            }

            if ( model.getFloorsInHouseFrom() !=0 )
            {
                httpRequest += "&minfloorn="+ Integer.toString(searchFiltersDal.getFloorsInHouseFrom(model.getFloorsInHouseFrom()));
            }

            if ( model.getFloorsInHouseTo() !=0 )
            {
                httpRequest += "&maxfloorn="+ Integer.toString(searchFiltersDal.getFloorsInHouseTo(model.getFloorsInHouseTo()));
            }

            if ( model.getKitchenSizeFrom() !=0 )
            {
                httpRequest += "&minkarea="+ Integer.toString(searchFiltersDal.getKitchenSizeFrom(model.getKitchenSizeFrom()));
            }

            if ( model.getKitchenSizeTo() !=0 )
            {
                httpRequest += "&maxkarea="+ Integer.toString(searchFiltersDal.getKitchenSizeTo(model.getKitchenSizeTo()));
            }
            int i=1;
        }
        catch (Exception ex)
        {
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
