package com.chicago.app.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
//import java.net.HttpURLConnection;
import javax.net.ssl.*;

public class TestCrawlerRequest {

    private  Elements _mainElements;
    private String _strInfo;
    private String _strAddress;
    private String _strHref;

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

    @Test
    public void testHttpRequest() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\ATolstov\\Downloads\\test.txt"));
        try{
            //String httpRequest ="https://spb.cian.ru/cat.php?currency=2&deal_type=sale&district%5B0%5D=147&engine_version=2&maxprice=20000000&metro%5B0%5D=215&maxkarea=15&object_type%5B0%5D=2&offer_type=flat&region=2&room1=1&sort=price_object_order";
            //String httpRequest ="https://spb.cian.ru/cat.php?currency=2&deal_type=sale&engine_version=2&maxlarea=150&maxprice=35000000&minprice=25000000&offer_type=flat&region=2&room3=1&sort=area_order";

           //URL obj = new URL(httpRequest);
           //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
           //con.setRequestMethod("GET");
           //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0");

           //System.out.println("Sending 'GET' request to URL : " + httpRequest);
           //System.out.println("Response Code : " + con.getResponseCode());

         /*   BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();*/

      //save result
      /*File fileMain = new File("C:\\Users\\ATolstov\\Downloads\\receive.html");
      FileUtils.writeStringToFile(fileMain, response.toString(), "UTF-8");//.outerHtml()
      System.out.println(response.toString());*/

       // load data
       File input = new File("C:\\Users\\ATolstov\\Downloads\\receive.html");
       org.jsoup.nodes.Document cianDoc = Jsoup.parse(input, "UTF-8");
       // org.jsoup.nodes.Document cianDoc = Jsoup.parse(response.toString());

       out.write("Total offers: " +  getTotalOffers( cianDoc )+"\n");
       _mainElements =  cianDoc.select("div[class^=c6e8ba5398--info--]");
       int i = 0;
       for (Element item : _mainElements )
       {
            //System.out.println("HTML: " + item.html());
           _strInfo = item.select("a[class^=c6e8ba5398--header--1fV2A]").text();
           _strAddress= item.select("div[class^=c6e8ba5398--address-links--]").select("span[content]").attr("content");
           _strHref= item.select("a[class^=c6e8ba5398--header--1fV2A]").attr("href");

            System.out.println("Offer: " + ++i);

            System.out.println("Rooms:" + getRoomsNumber());
            System.out.println("My floor:" + getFloor());
            System.out.println("Total floors:" + getFloorsInHouse());
            System.out.println("Price :" + getAptPrice(item));
            out.write("Offer: " + i +" Link: "  + _strHref + "; "+ getPropertyId() +";"+  getCityName() +"," + getStreetName() + ","+ getBuildingNumber()+ "; Rooms: " + getRoomsNumber() + "; Total square: " +  getAptSize() + "; My floor: " + getFloor() + "; Total floors: " + getFloorsInHouse() + "; Price: " + getAptPrice(item) + "\n");
       }
    }
        catch (IOException ex)
        {
            ex.printStackTrace();

        }
        finally{
            out.close();
        }
    }
}
