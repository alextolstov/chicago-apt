package com.chicago.ext.bll;

import com.chicago.dto.OrganizationOuterClass;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface OrganizationBll
{
    OrganizationOuterClass.Organization createOrganization(OrganizationOuterClass.Organization organization) throws Exception;

    void updateOrganization(OrganizationOuterClass.Organization organization);

    OrganizationOuterClass.Organization getOrganization(OrganizationOuterClass.Organization organization) throws Exception;

    OrganizationOuterClass.OrganizationInfo getOrganizationStructure(String userId) throws Exception;
}


