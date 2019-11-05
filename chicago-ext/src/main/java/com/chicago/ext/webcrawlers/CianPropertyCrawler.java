package com.chicago.ext.webcrawlers;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.PropertyModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CianPropertyCrawler implements PropertyCrawler<CianSearchFiltersModel.CianSearchFilters>
{
    private Elements _mainElements;
    private String _strInfo;
    private String _strAddress;
    private String _strHref;
    private Map<String, PropertyModel.Property> _map = new HashMap<String, PropertyModel.Property>();

    public int getTotalOffers( org.jsoup.nodes.Document cianDocument)
    {
        String strTitle = cianDocument.select("div[class^=_93444fe79c--totalOffers]").text();
        return Integer.parseInt(strTitle.split(" предложений")[0]);
    }

    public String getBuildingNumber()
    {
        String[] addrtokens = _strAddress.split(",");
        if (addrtokens.length == 5)
            return "";
        else
            return  addrtokens[5];
    }

    public String getCityName( )
    {
        return  _strAddress.split(",")[0];
    }

    public String getStreetName()
    {
        return  _strAddress.split(",")[4];
    }

    public int getAptPrice(Element element )
    {
        return  Integer.parseInt(element.select("div[class^=c6e8ba5398--header--1d]").text().replace(" \u20BD", "").replaceAll(" ",""));
    }

    public String getPropertyId()
    {
        String[] tokens  = _strHref.split("/");
        if (tokens.length == 6)
            return tokens[5];
        return"";
    }

    public int getRoomsNumber( )
    {
        String[] tokens = _strInfo.split(",");
        if (tokens.length > 2)
        {
            tokens[0] = tokens[0].substring( 0, tokens[0].indexOf('-') );
            return Integer.parseInt(tokens[0]);
        }
        return 0;
    }
    public float getAptSize( )
    {
        String[] tokens = _strInfo.split(",");
        if (tokens.length > 2)
        {
            if (tokens.length == 4)
                return Float.parseFloat(tokens[1].trim() + "." + tokens[2].replace(" м²", "").trim());
            else
                return Float.parseFloat(tokens[1].replace(" м²", "").trim());
        }
        return 0;
    }

    public int getFloor()
    {
        String[] tokens = _strInfo.split(",");
        String strFloor;
        if (tokens.length > 2)
        {
            if (tokens.length == 4)
                strFloor = tokens[3].replace(" этаж", "").split("/")[0];
            else
                strFloor = tokens[2].replace(" этаж", "").split("/")[0];
            return Integer.parseInt(strFloor.trim());
        }
        return 0;
    }

    public int getFloorsInHouse()
    {
        String[] tokens = _strInfo.split(",");
        String strFloors;
        if (tokens.length > 2)
        {
            if (tokens.length == 4)
                strFloors = tokens[3].replace(" этаж", "").split("/")[1];
            else
                strFloors = tokens[2].replace(" этаж", "").split("/")[1];
            return Integer.parseInt(strFloors);
        }
        return 0;
    }

    @Override
    public Map<String, PropertyModel.Property> GetProperties(CianSearchFiltersModel.CianSearchFilters searchFilters)
    {
        try
        {
            List<Integer> districts = searchFilters.getDistrictsId();
            List<Integer> stations = searchFilters.getSubwayStationsId();
            String str1 = URLEncoder.encode("[", "UTF-8");
            String str2 = URLEncoder.encode("]", "UTF-8");

            String httpRequest =  "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&region=" +searchFilters.getCityId();

            if( searchFilters.getTypeId() == EnumTypes.PropertyType.APARTMENT )
            {
                httpRequest += "&offer_type=flat";
            }

            if( searchFilters.getTypeId() == EnumTypes.PropertyType.ROOM )
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

            if ( searchFilters.getAptPriceFrom()!= 0 )
            {
                httpRequest += "&minprice=" + searchFilters.getAptPriceFrom();
            }

            if ( searchFilters.getAptPriceTo()!= 0 )
            {
                httpRequest += "&maxprice=" + searchFilters.getAptPriceTo();
            }

            if ( searchFilters.getAptSizeFrom() != 0 )
            {
                httpRequest += "&mintarea=" + searchFilters.getAptSizeFrom();
            }

            if ( searchFilters.getAptSizeTo() != 0 )
            {
                httpRequest += "&maxtarea=" + searchFilters.getAptSizeTo();
            }

            idx = 0;
            if ( searchFilters.getMarketId() == EnumTypes.Market.SECOND )
            {
                httpRequest += "&object_type" +str1 + "0" + str2 +  "=1";
            }

            if ( searchFilters.getMarketId() == EnumTypes.Market.FIRST )
            {
                httpRequest += "&object_type" +str1 + "0" + str2 +  "=2";
            }

            List<Integer> rooms = searchFilters.getRoomsNumber();
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

            if( searchFilters.isNotLastFloor())
            {
                httpRequest += "&floornl=1";
            }

            if( searchFilters.isLastFloor() )
            {
                httpRequest += "&floornl=0";
            }

            if( searchFilters.isNotFirstFloor() )
            {
                httpRequest += "&is_first_floor=0";
            }

            if ( searchFilters.getFloorFrom() != 0 )
            {
                httpRequest += "&minfloor=" + searchFilters.getFloorFrom();
            }

            if ( searchFilters.getFloorTo() != 0 )
            {
                httpRequest += "&maxfloor=" + searchFilters.getFloorTo();
            }

            if ( searchFilters.getFloorsInHouseFrom() != 0 )
            {
                httpRequest += "&minfloorn=" + searchFilters.getFloorsInHouseFrom();
            }

            if ( searchFilters.getFloorsInHouseTo() != 0 )
            {
                httpRequest += "&maxfloorn=" + searchFilters.getFloorsInHouseTo();
            }

            if ( searchFilters.getKitchenSizeFrom() != 0 )
            {
                httpRequest += "&minkarea=" + searchFilters.getKitchenSizeFrom();
            }

            if ( searchFilters.getKitchenSizeTo() != 0 )
            {
                httpRequest += "&maxkarea=" + searchFilters.getKitchenSizeTo();
            }
            URL obj = new URL(httpRequest);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0");
            //System.out.println("Response Code : " + con.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            org.jsoup.nodes.Document cianDoc = Jsoup.parse(response.toString());

            _mainElements =  cianDoc.select("div[class^=c6e8ba5398--info--]");
            //System.out.println("Total offers: " +  getTotalOffers( cianDoc )+"\n");
            for (Element item : _mainElements )
            {
                _strInfo = item.select("a[class^=c6e8ba5398--header--1fV2A]").text();
                _strAddress= item.select("div[class^=c6e8ba5398--address-links--]").select("span[content]").attr("content");
                _strHref= item.select("a[class^=c6e8ba5398--header--1fV2A]").attr("href");

                PropertyModel.Property propertyModelProperty = new PropertyModel.Property();

                propertyModelProperty.setRoomsNumber(getRoomsNumber());
                propertyModelProperty.setFloor(getFloor());
                propertyModelProperty.setFloorsInHouse(getFloorsInHouse());
                propertyModelProperty.setAptPrice(getAptPrice(item));
                propertyModelProperty.setAptSize(getAptSize());
                propertyModelProperty.setPropertyId(getPropertyId());
                propertyModelProperty.setCityName(getCityName());
                propertyModelProperty.setStreetName(getStreetName());
                propertyModelProperty.setBuildingNumber(getBuildingNumber());

                _map.put(_strHref, propertyModelProperty);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    return _map;
    }
}
