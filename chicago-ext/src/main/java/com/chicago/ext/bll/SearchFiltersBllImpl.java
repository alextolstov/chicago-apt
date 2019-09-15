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
