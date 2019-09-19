package com.chicago.ext.bll;

import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Contract;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Contract
public class SearchFiltersBllImpl implements SearchFiltersBll
{
    @Inject
    private SearchFiltersDal _searchFiltersDal;

    @Override
    public CianSearchFiltersModel.CianSearchFilters getCianSearchFilters(SearchFiltersModel.SearchFilters outSearchFilters) throws Exception
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
