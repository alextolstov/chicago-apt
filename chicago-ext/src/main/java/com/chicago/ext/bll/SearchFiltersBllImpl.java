package com.chicago.ext.bll;

import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import com.chicago.ext.webcrawlers.CianPropertyCrawler;
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
        CianSearchFiltersModel.CianSearchFilters cianFilter = new CianSearchFiltersModel.CianSearchFilters();

        cianFilter.setAptPriceFrom(_searchFiltersDal.getAptPriceFrom(outSearchFilters.getAptPriceFrom()));
        cianFilter.setAptPriceTo(_searchFiltersDal.getAptPriceTo(outSearchFilters.getAptPriceTo()));
        cianFilter.setAptSizeFrom(_searchFiltersDal.getAptSizeFrom(outSearchFilters.getAptSizeFrom()));
        cianFilter.setAptSizeTo(_searchFiltersDal.getAptSizeTo(outSearchFilters.getAptSizeTo()));
        cianFilter.setFloorFrom(_searchFiltersDal.getFloorFrom(outSearchFilters.getFloorFrom()));
        cianFilter.setFloorTo(_searchFiltersDal.getFloorFrom(outSearchFilters.getFloorTo()));
        cianFilter.setFloorsInHouseFrom(_searchFiltersDal.getFloorFrom(outSearchFilters.getFloorsInHouseFrom()));
        cianFilter.setFloorsInHouseTo(_searchFiltersDal.getFloorFrom(outSearchFilters.getFloorsInHouseTo()));
        cianFilter.setKitchenSizeFrom(_searchFiltersDal.getKitchenSizeFrom(outSearchFilters.getKitchenSizeFrom()));
        cianFilter.setKitchenSizeTo(_searchFiltersDal.getKitchenSizeTo(outSearchFilters.getKitchenSizeTo()));
        cianFilter.setRoomsNumber(_searchFiltersDal.getRoomsNumberList(outSearchFilters.getRoomsNumberList()));
        cianFilter.setLastFloor(_searchFiltersDal.isLastFloor(outSearchFilters.isLastFloor()));
        cianFilter.setNotFirstFloor(_searchFiltersDal.isNotFirstFloor(outSearchFilters.isNotFirstFloor()));
        cianFilter.setNotLastFloor(_searchFiltersDal.isNotLastFloor(outSearchFilters.isNotLastFloor()));
        cianFilter.setSubwayStationsId(outSearchFilters.getSubwayStationsList());
        cianFilter.setDistrictsId(outSearchFilters.getDistrictsList());
        cianFilter.setTypeId(outSearchFilters.getTypeId());
        cianFilter.setMarketId(outSearchFilters.getMarketId());
        //_searchFiltersDal.getSubwayStationsList(outSearchFilters.getSubwayStationsList());
        //_searchFiltersDal.getDistrictsList(outSearchFilters.getDistrictsList());
        //_searchFiltersDal.getPropertyType(outSearchFilters.getTypeId());
        //_searchFiltersDal.getMarket(outSearchFilters.getMarketId());
        CianPropertyCrawler propertyCrawler = new CianPropertyCrawler();
        propertyCrawler.GetProperties(cianFilter);
        return cianFilter;
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
