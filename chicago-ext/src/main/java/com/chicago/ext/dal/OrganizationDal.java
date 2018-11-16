package com.chicago.ext.dal;

import com.chicago.dto.Organization;

public interface OrganizationDal
{
    String createHolding(Organization.Holding holding);

    String createCompany(Organization.Company company);

    String createBranch(Organization.Branch branch);

    void addUserToCompany(String userId, String organizationId);
}
