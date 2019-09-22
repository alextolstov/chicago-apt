package com.chicago.ext.webcrawlers;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.PropertyModel;

import java.util.Map;

public class CianPropertyCrawler implements PropertyCrawler<CianSearchFiltersModel.CianSearchFilters>
{
    @Override
    public Map<String, PropertyModel.Property> GetProperties(CianSearchFiltersModel.CianSearchFilters searchFilters)
    {
        return null;
    }
}
