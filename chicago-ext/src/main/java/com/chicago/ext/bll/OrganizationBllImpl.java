package com.chicago.ext.bll;

import com.chicago.dto.OrganizationOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.UserDal;
import org.jvnet.hk2.annotations.Contract;

import javax.inject.Inject;

@Contract
public class OrganizationBllImpl implements OrganizationBll
{
    @Inject
    private OrganizationDal _organizationDal;
    @Inject
    private UserDal _userDal;

    @Override
    public OrganizationOuterClass.Organization createOrganization(OrganizationOuterClass.Organization organization) throws Exception
    {
        if (organization.getParentOrganizationId() == null)
        {
            throw new Exception("Parent organization should be provided.");
        }
        OrganizationOuterClass.Organization org = _organizationDal.getOrganization(organization.getParentOrganizationId());
        OrganizationOuterClass.OrganizationType orgType = OrganizationOuterClass.OrganizationType.HOLDING;

        switch (org.getType())
        {
            case HOLDING:
                orgType = OrganizationOuterClass.OrganizationType.COMPANY;
                break;
            case COMPANY:
                orgType = OrganizationOuterClass.OrganizationType.BRANCH;
                break;
            case BRANCH:
                throw new Exception("Can not add child into branch");
        }
        organization = OrganizationOuterClass.Organization.newBuilder(organization)
                .setType(orgType)
                .build();

        String organizationId = _organizationDal.createOrganization(organization);
        return OrganizationOuterClass.Organization.newBuilder(organization)
                .setOrganizationId(organizationId)
                .build();
    }

    @Override
    public void updateOrganization(OrganizationOuterClass.Organization organization)
    {
        _organizationDal.updateOrganization(organization);
    }

    @Override
    public OrganizationOuterClass.Organization getOrganization(OrganizationOuterClass.Organization organization) throws Exception
    {
        return _organizationDal.getOrganization(organization.getOrganizationId());
    }

    @Override
    public OrganizationOuterClass.OrganizationInfo getOrganizationStructure(String userId) throws Exception
    {
        UserOuterClass.User user = _userDal.getUserById(userId);
        user.getOrganizationId();
        return _organizationDal.getOrganizationStructure(user.getOrganizationId());
    }
}


