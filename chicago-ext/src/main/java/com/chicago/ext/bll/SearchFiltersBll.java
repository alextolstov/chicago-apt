package com.chicago.ext.bll;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface SearchFiltersBll
{
    CianSearchFiltersModel.CianSearchFilters getCianSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception;
    YandexSearchFiltersModel.YandexSearchFilters getYandexSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception;
    List<SearchFiltersModel.SearchFilters> getSearchFilters(String userId) throws Exception;
}
