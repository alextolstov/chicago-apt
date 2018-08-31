package com.chicago.ext.dal;

import com.chicago.dto.Organization;

public interface OrganizationDal
{
    Organization.Holding createHolding(Organization.Holding holding);

    Organization.Company createCompany(Organization.Company company);

    Organization.Branch createBranch(Organization.Branch branch);
}
