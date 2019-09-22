package com.chicago.ext.webcrawlers;

import com.chicago.ext.model.CianSearchFiltersModel;
import com.chicago.ext.model.PropertyModel;
import com.chicago.ext.model.SearchFiltersModel;

import java.util.Map;

public interface PropertyCrawler <T>
{
    /**
     * Get properties for CIAN
     * @param searchFilters
     * @return
     */
    Map<String, PropertyModel.Property> GetProperties(T searchFilters);
}
