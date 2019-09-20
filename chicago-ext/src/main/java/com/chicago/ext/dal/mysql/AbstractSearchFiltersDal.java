package com.chicago.ext.dal.mysql;

import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.SearchFiltersDal;
import com.chicago.ext.model.EnumTypes;
import com.chicago.ext.model.SearchFiltersModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractSearchFiltersDal implements SearchFiltersDal
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchFiltersDal.class);

    @Inject
    private DbConnector<Connection> _mySqlConnector;

    @Override
    public void addSearchFilter(String userId, String searchFilter) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spInsertSearchFilter(?, ?, ?) }");
        stmt.setString("userId", userId);
        stmt.setInt("filterId", searchFilter.hashCode());
        stmt.setString("searchFilter", searchFilter);
        stmt.executeQuery();
        stmt.close();
    }

    @Override
    public List<String> getSearchFilters(String userId) throws Exception
    {
        CallableStatement stmt = _mySqlConnector.getSession().prepareCall("{ call spGetSearchFilter(?) }");
        stmt.setString("userId", userId);
        stmt.executeQuery();
        ResultSet rs = stmt.executeQuery();
        List<String> filters = new ArrayList<>();
        while (rs.next())
        {
            filters.add(rs.getString("searchFilter"));
        }
        stmt.close();
        return filters;
    }
}
