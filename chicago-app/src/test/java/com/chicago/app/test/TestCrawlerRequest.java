package com.chicago.app.test;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TestCrawlerRequest
{
    @Test
    public void testHttpRequest() throws IOException
    {
        BufferedWriter out = new BufferedWriter(new FileWriter("C:/Temp/test.txt"));
        try
        {
            //String httpRequest ="https://spb.cian.ru/cat.php?currency=2&deal_type=sale&district%5B0%5D=147&engine_version=2&maxprice=20000000&metro%5B0%5D=215&maxkarea=15&object_type%5B0%5D=2&offer_type=flat&region=2&room1=1&sort=price_object_order";
            String httpRequest = "https://spb.cian.ru/cat.php?currency=2&deal_type=sale&engine_version=2&maxlarea=150&maxprice=35000000&minprice=25000000&offer_type=flat&region=2&room3=1&sort=area_order";

            URL obj = new URL(httpRequest);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0");

            System.out.println("Sending 'GET' request to URL : " + httpRequest);
            System.out.println("Response Code : " + con.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();

            //save result
            File fileMain = new File("C:/Temp/receive.html");
            FileUtils.writeStringToFile(fileMain, response.toString(), "UTF-8");//.outerHtml()
            System.out.println(response.toString());

            //Element body = doc.body();
            //File fileBody = new File("C:\\Users\\ATolstov\\Downloads\\filebody.html");
            //FileUtils.writeStringToFile(fileBody, body.html(), "UTF-8");

            // load data
            // File input = new File("C:\\Users\\ATolstov\\Downloads\\receive.html");
            //org.jsoup.nodes.Document mydoc = Jsoup.parse(input, "UTF-8");
            org.jsoup.nodes.Document mydoc = Jsoup.parse(response.toString());

            String strTitle = mydoc.select("div[class^=_93444fe79c--totalOffers]").text();
            String[] names = strTitle.split(" предложений");
            out.write("Total offers :" + names[0] + "\n");
            Elements mainElements = mydoc.select("div[class^=c6e8ba5398--info--]");// 1fcZi WcX5M
            //Elements prices  =  doc.select("div[class^=c6e8ba5398--header--1d]");
            //System.out.println("Elements " + mainElements.size());

            int i = 0;
            for (Element item : mainElements)
            {
                String strPrice = item.select("div[class^=c6e8ba5398--header--1d]").text().replace(" \u20BD", "").trim(); //1dF9r 1df-X
                String strInfo = item.select("div.c6e8ba5398--single_title--22TGT").text();
                if (strInfo.isEmpty())
                    strInfo = item.select("div.c6e8ba5398--title--2CW78").text();
                if (strInfo.isEmpty())
                    strInfo = item.select("div.c6e8ba5398--subtitle--UTwbQ").text();

                System.out.println("Offer: " + i++);
                String[] tokens = strInfo.split(",");

                if (tokens.length > 2)
                {
                    tokens[0] = tokens[0].substring(0, tokens[0].indexOf('-'));
                    System.out.println("Rooms:" + tokens[0]);
                    String[] floors;
                    if (tokens.length == 4)
                    {
                        tokens[1] = tokens[1].trim();
                        tokens[2] = tokens[2].replace(" м²", "").trim();
                        System.out.println("Total square:" + tokens[1]);
                        floors = tokens[3].replace(" этаж", "").split("/");
                    } else
                    {
                        tokens[1] = tokens[1].replace(" м²", "").trim();
                        System.out.println("Total square:" + tokens[1]);
                        floors = tokens[2].replace(" этаж", "").split("/");
                    }
                    if (floors.length > 1)
                    {
                        System.out.println("My floor:" + floors[0].trim());
                        System.out.println("Total floors:" + floors[1]);
                        System.out.println("Price :" + strPrice);
                        if (tokens.length == 4)
                            out.write("Offer: " + i + "; Rooms: " + tokens[0] + "; Total square: " + tokens[1] + "," + tokens[2] + "; My floor: " + floors[0].trim() + "; Total floors: " + floors[1] + "; Price: " + strPrice + "\n");
                        else
                            out.write("Offer: " + i + "; Rooms: " + tokens[0] + "; Total square: " + tokens[1] + "; My floor: " + floors[0].trim() + "; Total floors: " + floors[1] + "; Price: " + strPrice + "\n");
                    }
                }
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();

        } finally
        {
            out.close();
        }
    }
}
//org.jsoup.Connection  con = org.jsoup.helper.HttpConnection.connect(httpRequest);
// c6e8ba5398--single_title--22TGT - "1-комн. кв., 36 м², 25/25 этаж"

//  c6e8ba5398--title--39crr - "Светлая и просторная 1ккв 40,7м2"
// c6e8ba5398--subtitle--UTwbQ - "1-комн. кв., 41 м², 15/21 этаж"

// c6e8ba5398--single_title--22TGT - "1-комн. кв., 37 м², 8/12 этаж"
// c6e8ba5398--single_title--22TGT - "2-комн. кв., 45 м², 2/9 этаж"

// c6e8ba5398--title--39crr - "Лучшая цена в Приморском районе "
// c6e8ba5398--subtitle--UTwbQ" - "1-комн. кв., 37 м², 5/19 этаж"

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


