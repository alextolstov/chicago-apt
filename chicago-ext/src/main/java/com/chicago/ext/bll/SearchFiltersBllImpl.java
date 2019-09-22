package com.chicago.ext.bll;

import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Contract;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Contract
public class SearchFiltersBllImpl implements SearchFiltersBll
{
    @Inject
    private SearchFiltersDal _searchFiltersDal;

    private Gson _gson = new Gson();

    @Override
    public CianSearchFiltersModel.CianSearchFilters getCianSearchFilters(SearchFiltersModel.SearchFilters outSearchFilters) throws Exception
    {
        List<String> districts = _searchFiltersDal.getDistrictsList(outSearchFilters.getDistrictsList());
        List<String> stations = _searchFiltersDal.getSubwayStationsList(outSearchFilters.getSubwayStationsList());

        String str1 = URLEncoder.encode("[", "UTF-8");
        String str2 = URLEncoder.encode("]", "UTF-8");

        String httpRequest =  "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&region=" +_searchFiltersDal.getCityId(outSearchFilters.getCityId());

        if( outSearchFilters.getTypeId() == EnumTypes.PropertyType.APARTMENT )
        {
            httpRequest += "&offer_type=flat";
        }

        if( outSearchFilters.getTypeId() == EnumTypes.PropertyType.ROOM )
        {
            httpRequest += "&offer_type=flat&room0=1";
        }

        int idx = 0;
        StringBuilder sb = new StringBuilder();
        for (String station : stations)
        {
            sb.append("&metro").append(str1).append(idx++).append(str2).append("=").append(station);
        }
        httpRequest += sb.toString();

        idx = 0;
        sb.setLength(0);
        for (String district : districts)
        {
            sb.append("&district").append(str1).append(idx++).append(str2).append("=").append(district);
        }
        httpRequest += sb.toString();

        if ( outSearchFilters.getAptPriceFrom()!= 0 )
        {
            httpRequest += "&minprice=" + _searchFiltersDal.getAptPriceFrom(outSearchFilters.getAptPriceFrom());
        }

        if ( outSearchFilters.getAptPriceTo()!= 0 )
        {
            httpRequest += "&maxprice=" + _searchFiltersDal.getAptPriceTo(outSearchFilters.getAptPriceTo());
        }

        if ( outSearchFilters.getAptSizeFrom() != 0 )
        {
            httpRequest += "&mintarea=" + _searchFiltersDal.getAptSizeFrom(outSearchFilters.getAptSizeFrom());
        }

        if ( outSearchFilters.getAptSizeTo() != 0 )
        {
            httpRequest += "&maxtarea=" + _searchFiltersDal.getAptSizeTo(outSearchFilters.getAptSizeTo());
        }

        idx = 0;
        if ( outSearchFilters.getMarketId() == EnumTypes.Market.SECOND )
        {
            httpRequest += "&object_type" +str1 + "0" + str2 +  "=1";
        }

        if ( outSearchFilters.getMarketId() == EnumTypes.Market.FIRST )
        {
            httpRequest += "&object_type" +str1 + "0" + str2 +  "=2";
        }

        List<Integer> rooms = outSearchFilters.getRoomsNumberList();
        sb.setLength(0);
        for (int room : rooms)
        {
            switch (room) {
                case 0:
                    sb.append("&room9=1"); //&room0 just a room, not appartment
                    break;
                case 1:
                    sb.append("&room1=1");
                    break;
                case 2:
                    sb.append("&room2=1");
                    break;
                case 3:
                    sb.append("&room3=1");
                    break;
                case 4:
                    sb.append("&room4=1");
                    break;
                case 5:
                    sb.append("&room5=1");
                    break;
                case 6:
                    sb.append("&room6=1");
                    break;
                default:
                    sb.append("&room9=0"); // appartment
                    break;
            }
        }
        httpRequest += sb.toString();

        if( outSearchFilters.isNotLastFloor())
        {
            httpRequest += "&floornl=1";
        }

        if( outSearchFilters.isLastFloor() )
        {
            httpRequest += "&floornl=0";
        }

        if( outSearchFilters.isNotFirstFloor() )
        {
            httpRequest += "&is_first_floor=0";
        }

        if ( outSearchFilters.getFloorFrom() != 0 )
        {
            httpRequest += "&minfloor=" + _searchFiltersDal.getFloorFrom(outSearchFilters.getFloorFrom());
        }

        if ( outSearchFilters.getFloorTo() != 0 )
        {
            httpRequest += "&maxfloor=" + _searchFiltersDal.getFloorTo(outSearchFilters.getFloorTo());
        }

        if ( outSearchFilters.getFloorsInHouseFrom() != 0 )
        {
            httpRequest += "&minfloorn=" + _searchFiltersDal.getFloorsInHouseFrom(outSearchFilters.getFloorsInHouseFrom());
        }

        if ( outSearchFilters.getFloorsInHouseTo() != 0 )
        {
            httpRequest += "&maxfloorn=" + _searchFiltersDal.getFloorsInHouseTo(outSearchFilters.getFloorsInHouseTo());
        }

        if ( outSearchFilters.getKitchenSizeFrom() != 0 )
        {
            httpRequest += "&minkarea=" + _searchFiltersDal.getKitchenSizeFrom(outSearchFilters.getKitchenSizeFrom());
        }

        if ( outSearchFilters.getKitchenSizeTo() != 0 )
        {
            httpRequest += "&maxkarea=" + _searchFiltersDal.getKitchenSizeTo(outSearchFilters.getKitchenSizeTo());
        }

        return null;
    }

    @Override
    public YandexSearchFiltersModel.YandexSearchFilters getYandexSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception
    {
        return null;
    }

    @Override
    public List<SearchFiltersModel.SearchFilters> getSearchFilters(String userId) throws Exception
    {
        List<String> strFilters = _searchFiltersDal.getSearchFilters(userId);
        List<SearchFiltersModel.SearchFilters> searchfilters = new ArrayList<>();
        Gson gson = new Gson();

        for (String filter : strFilters)
        {
            SearchFiltersModel.SearchFilters obj = gson.fromJson(filter, SearchFiltersModel.SearchFilters.class);
            searchfilters.add(obj);
        }
        return searchfilters;
    }
}
