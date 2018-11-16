package com.chicago.ext.dal.cassandra;

import com.chicago.common.util.TimeUtil;
import com.chicago.dto.Organization;
import com.chicago.ext.dal.OrganizationDal;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.COMPANIES_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;

public class OrganizationDalImpl implements OrganizationDal
{
    private static final Logger LOG = LoggerFactory.getLogger(UserDalImpl.class);
    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public String createHolding(Organization.Holding holding)
    {
        return null;
    }

    @Override
    public String createCompany(Organization.Company company)
    {
        UUID organizationId = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, COMPANIES_TABLE)
                .value("organization_id", organizationId)
                .value("name", company.getName())
                .value("description", company.getDescription())
                .value("web_site", company.getWebSite())
                .value("email_domain", company.getEmailDomain())
                .value("phones", company.getPhonesList())
                .value("fax", company.getFaxList())
                .value("users", company.getUsersList())
                .value("branches", company.getBranchesList())
                .value("address_id", company.getAddresId())
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _cassandraConnector.getSession().execute(query);

        return organizationId.toString();
    }

    @Override
    public void addUserToCompany(String userId, String organizationId)
    {
        Statement query = QueryBuilder.update(KEYSPACE, COMPANIES_TABLE)
                .with(QueryBuilder.add("users", UUID.fromString(userId)))
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public String createBranch(Organization.Branch branch)
    {
        return null;
    }
}
