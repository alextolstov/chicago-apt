package com.chicago.ext.bll;

import com.chicago.ext.components.SearchFiltersRequests;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import com.chicago.ext.webcrawlers.CianPropertyCrawler;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Contract
public class SearchFiltersBllImpl implements SearchFiltersBll
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchFiltersBllImpl.class);

    @Inject
    private SearchFiltersDal _searchFiltersDal;

    private Gson _gson = new Gson();

    @Override
    public CianSearchFiltersModel.CianSearchFilters getCianSearchFilters(SearchFiltersModel.SearchFilters searchFilters) throws Exception
    {
        _searchFiltersDal.addSearchFilter(searchFilters.getUserId(), _gson.toJson(searchFilters, SearchFiltersModel.SearchFilters.class));

        CianSearchFiltersModel.CianSearchFilters cianFilter = new CianSearchFiltersModel.CianSearchFilters();

        cianFilter.setAptPriceFrom(_searchFiltersDal.getAptPriceFrom(searchFilters.getAptPriceFrom()));
        cianFilter.setAptPriceTo(_searchFiltersDal.getAptPriceTo(searchFilters.getAptPriceTo()));
        cianFilter.setAptSizeFrom(_searchFiltersDal.getAptSizeFrom(searchFilters.getAptSizeFrom()));
        cianFilter.setAptSizeTo(_searchFiltersDal.getAptSizeTo(searchFilters.getAptSizeTo()));
        cianFilter.setFloorFrom(_searchFiltersDal.getFloorFrom(searchFilters.getFloorFrom()));
        cianFilter.setFloorTo(_searchFiltersDal.getFloorFrom(searchFilters.getFloorTo()));
        cianFilter.setFloorsInHouseFrom(_searchFiltersDal.getFloorFrom(searchFilters.getFloorsInHouseFrom()));
        cianFilter.setFloorsInHouseTo(_searchFiltersDal.getFloorFrom(searchFilters.getFloorsInHouseTo()));
        cianFilter.setKitchenSizeFrom(_searchFiltersDal.getKitchenSizeFrom(searchFilters.getKitchenSizeFrom()));
        cianFilter.setKitchenSizeTo(_searchFiltersDal.getKitchenSizeTo(searchFilters.getKitchenSizeTo()));
        cianFilter.setRoomsNumber(_searchFiltersDal.getRoomsNumberList(searchFilters.getRoomsNumberList()));
        cianFilter.setLastFloor(_searchFiltersDal.isLastFloor(searchFilters.isLastFloor()));
        cianFilter.setNotFirstFloor(_searchFiltersDal.isNotFirstFloor(searchFilters.isNotFirstFloor()));
        cianFilter.setNotLastFloor(_searchFiltersDal.isNotLastFloor(searchFilters.isNotLastFloor()));

        ArrayList<Integer> stations = new ArrayList<>();
        for (SearchFiltersModel.SubwayStation s : searchFilters.getSubwayStationsList())
        {
            stations.add(s.getStationId());
        }
        cianFilter.setSubwayStationsId(stations);

        ArrayList<Integer> districts = new ArrayList<>();
        for (SearchFiltersModel.District d : searchFilters.getDistrictsList())
        {
            districts.add(d.getDistrictId());
        }

        cianFilter.setDistrictsId(districts);
        cianFilter.setTypeId(searchFilters.getTypeId());
        cianFilter.setMarketId(searchFilters.getMarketId());
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
            try
            {
                SearchFiltersModel.SearchFilters obj = gson.fromJson(filter, SearchFiltersModel.SearchFilters.class);
                searchfilters.add(obj);
            }
            catch (Exception ex)
            {
                LOG.warn("Failed to deserialize JSON :{}", filter);
            }
        }
        return searchfilters;
    }
}
