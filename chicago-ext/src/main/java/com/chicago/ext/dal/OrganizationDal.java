package com.chicago.ext.dal;

import com.chicago.dto.OrganizationOuterClass;

public interface OrganizationDal
{
    String createOrganization(OrganizationOuterClass.Organization holding) throws Exception;

    void updateOrganization(OrganizationOuterClass.Organization organization);

    OrganizationOuterClass.Organization getOrganization(String organizationId) throws Exception;

    void addUserToCompany(String userId, String organizationId);

    OrganizationOuterClass.OrganizationInfo getOrganizationStructure(String organizationId) throws Exception;
}
