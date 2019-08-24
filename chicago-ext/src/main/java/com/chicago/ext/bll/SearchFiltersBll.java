package com.chicago.ext.bll;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface SearchFiltersBll
{
    CianSearchFiltersModel getCianSearchFilters(SearchFiltersModel ourSearchFilters) throws Exception;
    YandexSearchFiltersModel getYandexSearchFilters(SearchFiltersModel ourSearchFilters) throws Exception;
}
