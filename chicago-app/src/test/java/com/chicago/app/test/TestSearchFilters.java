package com.chicago.app.test;

import com.chicago.dto.CityOuterClass;
import com.chicago.dto.Searchfilters;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.model.CityModel;
import com.chicago.ext.model.SearchFiltersModel;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TestSearchFilters
{
    @Test
    public void testReadCity()
    {
        Searchfilters.SearchFilters searchFiltersDto = Searchfilters.SearchFilters.newBuilder()
                .setAptPriceFrom(10000)
                .setAptPriceTo(100000)
                .build();

        SearchFiltersModel.SearchFiltersConvertor cnv = new SearchFiltersModel.SearchFiltersConvertor();
        SearchFiltersModel.SearchFilters model = cnv.fromDto(searchFiltersDto);
        Assert.assertEquals(model.getAptPriceFrom(), 10000);
    }
}
