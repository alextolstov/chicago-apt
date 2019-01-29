package com.chicago.ext.dal.cassandra;

import com.chicago.common.util.TimeUtil;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.OrganizationNotFoundException;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.ORGANIZATIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraUtil.convertToUuidSet;

public class OrganizationDalImpl implements OrganizationDal
{
    private static final Logger LOG = LoggerFactory.getLogger(UserDalImpl.class);
    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public String createOrganization(OrganizationOuterClass.Organization organization) throws Exception
    {
        UUID organizationId = UUIDs.random();
        UUID parentId = null;
        UUID entityId = null;

        if (organization.getType() == OrganizationOuterClass.OrganizationType.HOLDING)
        {
            entityId = organizationId;
        } else
        {
            if (organization.getType() == OrganizationOuterClass.OrganizationType.COMPANY)
            {
                entityId = UUID.fromString(organization.getParentOrganizationId());
            } else if (organization.getType() == OrganizationOuterClass.OrganizationType.BRANCH)
            {
                entityId = getTopLevelOrganizationId(organization.getParentOrganizationId());
            }
            parentId = UUID.fromString(organization.getParentOrganizationId());
        }

        Statement query = QueryBuilder.insertInto(KEYSPACE, ORGANIZATIONS_TABLE)
                .value("organization_id", organizationId) // Already UUID
                .value("organization_type", organization.getType().getNumber()) // Enum
                .value("name", organization.getName())
                .value("description", organization.getDescription())
                .value("parent_organization_id", parentId)
                .value("entity_id", entityId)
                .value("web_site", organization.getWebSite())
                .value("email_domain", organization.getEmailDomain())
                .value("phones", organization.getPhonesList())
                .value("fax", organization.getFaxList())
                // Address already created as first step when holding saved
                .value("address_id", organization.getAddressId().length() == 0 ? null : UUID.fromString(organization.getAddressId()))
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _cassandraConnector.getSession().execute(query);

        // Add to company holdings list
        if (organization.getType() != OrganizationOuterClass.OrganizationType.HOLDING)
        {
            query = QueryBuilder.update(KEYSPACE, ORGANIZATIONS_TABLE)
                    .with(QueryBuilder.add("child_organizations", organizationId))
                    .where(QueryBuilder.eq("organization_id", parentId));
            _cassandraConnector.getSession().execute(query);
        }
        return organizationId.toString();
    }

    @Override
    public void updateOrganization(OrganizationOuterClass.Organization organization)
    {
        Set<UUID> usersSet = convertToUuidSet(organization.getUsersList());
        Set<UUID> childOrganizationsSet = convertToUuidSet(organization.getOrganizationsList());
        Statement query = QueryBuilder.update(KEYSPACE, ORGANIZATIONS_TABLE)
                .with(QueryBuilder.set("name", organization.getName()))
                .and(QueryBuilder.set("description", organization.getDescription()))
                .and(QueryBuilder.set("web_site", organization.getWebSite()))
                .and(QueryBuilder.set("email_domain", organization.getEmailDomain()))
                .and(QueryBuilder.set("phones", organization.getPhonesList()))
                .and(QueryBuilder.set("fax", organization.getFaxList()))
                .and(QueryBuilder.set("child_organizations", childOrganizationsSet))
                .and(QueryBuilder.set("address_id", organization.getAddressId().length() == 0 ? null : UUID.fromString(organization.getAddressId())))
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organization.getOrganizationId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public OrganizationOuterClass.Organization getOrganization(String organizationId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ORGANIZATIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        return buildOrganization(result, organizationId);
    }

    @Override
    public void addUserToCompany(String userId, String organizationId)
    {
        Statement query = QueryBuilder.update(KEYSPACE, ORGANIZATIONS_TABLE)
                .with(QueryBuilder.add("users", UUID.fromString(userId)))
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public OrganizationOuterClass.OrganizationInfo getOrganizationStructure(String organizationId) throws Exception
    {
        UUID topLevelOrganizationId = getTopLevelOrganizationId(organizationId);

        OrganizationOuterClass.OrganizationInfo.Builder organizationInfoBuilder = OrganizationOuterClass.OrganizationInfo.newBuilder();
        traverseOrganizations(organizationInfoBuilder, topLevelOrganizationId);

        return organizationInfoBuilder.build();
    }

    private UUID getTopLevelOrganizationId(String organizationId)
    {
        UUID topLevelOrganizationId;
        UUID uuidOrganizationId = UUID.fromString(organizationId);

        for (; ; )
        {
            Statement query = QueryBuilder.select()
                    .from(KEYSPACE, ORGANIZATIONS_TABLE)
                    .where(QueryBuilder.eq("organization_id", uuidOrganizationId));
            ResultSet result = _cassandraConnector.getSession().execute(query);
            Row row = result.one();
            UUID tempOrganizationId;
            if ((tempOrganizationId = row.getUUID("parent_organization_id")) == null)
            {
                topLevelOrganizationId = uuidOrganizationId;
                break;
            }
            uuidOrganizationId = tempOrganizationId;
        }
        return topLevelOrganizationId;
    }

    private void traverseOrganizations(OrganizationOuterClass.OrganizationInfo.Builder builder, UUID organizationId) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ORGANIZATIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", organizationId));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        Set<UUID> childOrganizations = row.getSet("child_organizations", UUID.class);

        builder.setOrganizationId(organizationId.toString());
        builder.setType(toTypeEnum(row.getInt("organization_type")));
        builder.setName(row.getString("name"));

        if (childOrganizations == null)
        {
            return;
        }
        for (UUID childId : childOrganizations)
        {
            OrganizationOuterClass.OrganizationInfo.Builder nextLevelBuilder = builder.addOrganizationsBuilder();
            traverseOrganizations(nextLevelBuilder, childId);
        }
    }

    private OrganizationOuterClass.OrganizationType toTypeEnum(int type) throws Exception
    {
        switch (type)
        {
            case 0:
                return OrganizationOuterClass.OrganizationType.HOLDING;
            case 1:
                return OrganizationOuterClass.OrganizationType.COMPANY;
            case 2:
                return OrganizationOuterClass.OrganizationType.BRANCH;
        }
        throw new Exception("Unknown organization type");
    }

    private OrganizationOuterClass.Organization buildOrganization(ResultSet result, String organizationId) throws Exception
    {
        Row row = result.one();
        if (row == null)
        {
            throw new OrganizationNotFoundException("No holding with id " + organizationId + " found");
        }

        OrganizationOuterClass.Organization.Builder builder = OrganizationOuterClass.Organization.newBuilder();
        builder.setOrganizationId(organizationId);
        builder.setType(toTypeEnum(row.getInt("organization_type")));

        String tempField;
        if ((tempField = row.getString("name")) != null)
        {
            builder.setName(tempField);
        }
        if ((tempField = row.getString("description")) != null)
        {
            builder.setDescription(tempField);
        }

        UUID addressId;
        if ((addressId = row.getUUID("address_id")) != null)
        {
            builder.setAddressId(addressId.toString());
        }

        Set<String> phones = row.getSet("phones", String.class);
        if (phones != null)
        {
            phones.forEach(builder::addPhones);
        }

        Set<String> faxes = row.getSet("fax", String.class);
        if (faxes != null)
        {
            faxes.forEach(builder::addFax);
        }

        Set<UUID> users = row.getSet("users", UUID.class);
        if (users != null)
        {
            users.stream().map(UUID::toString).forEach(builder::addUsers);
        }

        // Custom fields
        if (result.getColumnDefinitions().contains("web_site"))
        {
            if ((tempField = row.getString("web_site")) != null)
            {
                builder.setWebSite(tempField);
            }
        }

        if (result.getColumnDefinitions().contains("email_domain"))
        {
            if ((tempField = row.getString("email_domain")) != null)
            {
                builder.setEmailDomain(tempField);
            }
        }

        if (result.getColumnDefinitions().contains("companies"))
        {
            Set<UUID> companies = row.getSet("companies", UUID.class);
            if (companies != null)
            {
                companies.stream().map(UUID::toString).forEach(builder::addOrganizations);
            }
        }

        return builder.build();
    }
}
