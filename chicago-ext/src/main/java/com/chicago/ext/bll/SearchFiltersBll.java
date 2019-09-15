package com.chicago.ext.bll;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface SearchFiltersBll
{
    CianSearchFiltersModel getCianSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception;
    YandexSearchFiltersModel getYandexSearchFilters(SearchFiltersModel.SearchFilters ourSearchFilters) throws Exception;
}
