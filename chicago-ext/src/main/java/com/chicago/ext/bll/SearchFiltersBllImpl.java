package com.chicago.ext.bll;

import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import org.jvnet.hk2.annotations.Contract;

import javax.inject.Inject;

@Contract
public class SearchFiltersBllImpl implements SearchFiltersBll
{
    @Inject
    private SearchFiltersDal _searchFiltersDal;

    @Override
    public CianSearchFiltersModel getCianSearchFilters(SearchFiltersModel.SearchFilters outSearchFilters) throws Exception
    {
        String httpRequest = "https://spb.cian.ru&parm1={1}&{2}";
        _searchFiltersDal.getCityId(outSearchFilters.getCityId());
        _searchFiltersDal.getDistrictsList(outSearchFilters.getDistrictsList());
        _searchFiltersDal.getPriceFrom(outSearchFilters.getAptPriceFrom());
        _searchFiltersDal.getPriceTo(outSearchFilters.getAptPriceTo());
        //outSearchFilters.getAptSizeFrom();
        //outSearchFilters.getAptSizeTo();
         //CianSearchFiltersDalImpl
        return  null;
    }

    @Override
    public YandexSearchFiltersModel getYandexSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception
    {
        return null;
    }
}
/* Common data for Cian

Купить 1-комнатную квартиру в Санкт-Петербурге
https://www.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=flat&p=2&region=2&room1=1

%5B0%5D []
Base url - https://spb.cian.ru/cat.php?currency=2 валюта рубли
                                       &deal_type=sale купить
                                       &engine_version=2
                                       &offer_type=flat квартира
                                       &region=2 регион Санкт Петербург
                                       &room9=1 студия
                                       &room0=1 комната
                                       &room1=1 1 комната
                                       &room2=1 2 комната
                                       &room3=1 3 команта
                                       &room6=1 6 комант
                                       &object_type%5B0%5D= 1 вторичка, 2 новостройка
                                       &m2=1 цена за м2 (по умолчанию за всё)
                                       &maxprice максимальная цена
                                       &minprice минимальная цена
                                       &is_by_homeowner=1 от собственника
                                       &maxfloor - максимальный этаж
                                       &minfloor - минимальный этаж
                                       &is_first_floor=0 - не первый этаж
                                       &floornl=1 - не последний этаж
                                       &floornl=0 - последний этаж
                                       &maxtarea - максимальная общая площадь
                                       &mintarea - минимальная общая площадь
                                       &minlarea - минимальная жилая площадь
                                       &maxlarea - максимальная жилая площадь
                                       &minkarea - минимальная площадь кухни
                                       &maxkarea - максимальная площадь кухни
                                       &street%5B0%5D = код улицы
                                       &district%5B0%5D = код района
                                       spb.cian.ru	/api/geo/get-districts-tree/?locationId=2
                                       &metro%5B0%5D = код станции метро
 */


