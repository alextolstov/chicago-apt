package com.chicago.ext.bll;

import com.chicago.ext.dal.mysql.CianSearchFiltersDalImpl;
import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.model.YandexSearchFiltersModel;
import org.jvnet.hk2.annotations.Contract;

@Contract
public class SearchFiltersBllImpl implements SearchFiltersBll
{
    @Override
    public CianSearchFiltersModel getCianSearchFilters(SearchFiltersModel outSearchFilters) throws Exception
    {
        //CianSearchFiltersDalImpl
        return  null;
    }

    @Override
    public YandexSearchFiltersModel getYandexSearchFilters(SearchFiltersModel ourSearchFilters) throws Exception
    {
        return null;
    }
}


