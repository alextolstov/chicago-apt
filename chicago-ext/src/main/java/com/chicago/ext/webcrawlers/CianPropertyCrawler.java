package com.chicago.ext.webcrawlers;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.PropertyModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class CianPropertyCrawler implements PropertyCrawler<CianSearchFiltersModel.CianSearchFilters>
{
    @Override
    public Map<String, PropertyModel.Property> GetProperties(CianSearchFiltersModel.CianSearchFilters searchFilters)
    {
        try
        {
            List<Integer> districts = searchFilters.getDistrictsId();
            List<Integer> stations = searchFilters.getSubwayStationsId();
            String str1 = URLEncoder.encode("[", "UTF-8");
            String str2 = URLEncoder.encode("]", "UTF-8");

            String httpRequest = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&region=" + searchFilters.getCityId();

            if (searchFilters.getTypeId() == EnumTypes.PropertyType.APARTMENT)
            {
                httpRequest += "&offer_type=flat";
            }

            if (searchFilters.getTypeId() == EnumTypes.PropertyType.ROOM)
            {
                httpRequest += "&offer_type=flat&room0=1";
            }

            int idx = 0;
            StringBuilder sb = new StringBuilder();
            for (Integer station : stations)
            {
                sb.append("&metro").append(str1).append(idx++).append(str2).append("=").append(station);
            }
            httpRequest += sb.toString();

            idx = 0;
            sb.setLength(0);
            for (Integer district : districts)
            {
                sb.append("&district").append(str1).append(idx++).append(str2).append("=").append(district);
            }
            httpRequest += sb.toString();

            if (searchFilters.getAptPriceFrom() != 0)
            {
                httpRequest += "&minprice=" + searchFilters.getAptPriceFrom();
            }

            if (searchFilters.getAptPriceTo() != 0)
            {
                httpRequest += "&maxprice=" + searchFilters.getAptPriceTo();
            }

            if (searchFilters.getAptSizeFrom() != 0)
            {
                httpRequest += "&mintarea=" + searchFilters.getAptSizeFrom();
            }

            if (searchFilters.getAptSizeTo() != 0)
            {
                httpRequest += "&maxtarea=" + searchFilters.getAptSizeTo();
            }

            idx = 0;
            if (searchFilters.getMarketId() == EnumTypes.Market.SECOND)
            {
                httpRequest += "&object_type" + str1 + "0" + str2 + "=1";
            }

            if (searchFilters.getMarketId() == EnumTypes.Market.FIRST)
            {
                httpRequest += "&object_type" + str1 + "0" + str2 + "=2";
            }

            List<Integer> rooms = searchFilters.getRoomsNumber();
            sb.setLength(0);
            for (int room : rooms)
            {
                switch (room)
                {
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

            if (searchFilters.isNotLastFloor())
            {
                httpRequest += "&floornl=1";
            }

            if (searchFilters.isLastFloor())
            {
                httpRequest += "&floornl=0";
            }

            if (searchFilters.isNotFirstFloor())
            {
                httpRequest += "&is_first_floor=0";
            }

            if (searchFilters.getFloorFrom() != 0)
            {
                httpRequest += "&minfloor=" + searchFilters.getFloorFrom();
            }

            if (searchFilters.getFloorTo() != 0)
            {
                httpRequest += "&maxfloor=" + searchFilters.getFloorTo();
            }

            if (searchFilters.getFloorsInHouseFrom() != 0)
            {
                httpRequest += "&minfloorn=" + searchFilters.getFloorsInHouseFrom();
            }

            if (searchFilters.getFloorsInHouseTo() != 0)
            {
                httpRequest += "&maxfloorn=" + searchFilters.getFloorsInHouseTo();
            }

            if (searchFilters.getKitchenSizeFrom() != 0)
            {
                httpRequest += "&minkarea=" + searchFilters.getKitchenSizeFrom();
            }

            if (searchFilters.getKitchenSizeTo() != 0)
            {
                httpRequest += "&maxkarea=" + searchFilters.getKitchenSizeTo();
            }
            org.jsoup.nodes.Document doc = Jsoup.connect(httpRequest).get();
            //org.jsoup.Connection  con = org.jsoup.helper.HttpConnection.connect(httpRequest);
            Element body = doc.body();
            Elements divsPrice = body.getElementsByClass("c6e8ba5398--header--1dF9r");
            Elements divsPricePerSquare = body.getElementsByClass("c6e8ba5398--term--3kvtJ");
            // c6e8ba5398--single_title--22TGT - "1-комн. кв., 36 м², 25/25 этаж"

            //  c6e8ba5398--title--39crr - "Светлая и просторная 1ккв 40,7м2"
            // c6e8ba5398--subtitle--UTwbQ - "1-комн. кв., 41 м², 15/21 этаж"

            // c6e8ba5398--single_title--22TGT - "1-комн. кв., 37 м², 8/12 этаж"
            // c6e8ba5398--single_title--22TGT - "2-комн. кв., 45 м², 2/9 этаж"

            // c6e8ba5398--title--39crr - "Лучшая цена в Приморском районе "
            // c6e8ba5398--subtitle--UTwbQ" - "1-комн. кв., 37 м², 5/19 этаж"

            Elements total = body.getElementsByClass("_93444fe79c--totalOffers--22-FL");
            // paging (page 2) class="_93444fe79c--list-item--2KxXr">
            // https://spb.cian.ru/cat.php?currency=2&deal_type=sale&engine_version=2&maxprice=5000000&object_type%5B0%5D=1&offer_type=flat
            // &p=2&region=2&room1=1&room2=1&room3=1

            // real elements
            // class="_93444fe79c--wrapper--E9jWb">
            // repeated   class="_93444fe79c--card--_yguQ">

            // top container
            // class="c6e8ba5398--offer-container--2sOFx c6e8ba5398--top3--1QmHz" data-reactroot=""
            // class="c6e8ba5398--container--Y5gG9"
            // class="c6e8ba5398--main-container--1FMpY"

            // just elements
            // class="c6e8ba5398--main--332Qx"
            // class="c6e8ba5398--info-container--A11gU"
            // class="c6e8ba5398--info--1fcZi"
            // class="undefined c6e8ba5398--main-info--oWcMk"







/*


            private String propertyId;
            private byte[] floorPlan;
            private EnumTypes.PropertyType typeId;

            private EnumTypes.ViewFromWindow windowsView;
            private Boolean balcony;
            private float kitchenSize;
            private float ceilingHeight;
            private int floor;
            private int floorsInHouse;
            private int roomsNumber;
            */
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
