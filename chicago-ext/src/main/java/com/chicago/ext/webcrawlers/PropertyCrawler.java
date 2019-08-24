package com.chicago.ext.webcrawlers;

import com.chicago.ext.model.PropertyModel;
import com.chicago.ext.model.SearchFiltersModel;

import java.util.Map;

public interface PropertyCrawler
{
    Map<String, PropertyModel.Property> GetProperties(SearchFiltersModel.SearchFilters searchFilters);
}
