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
    public OrganizationOuterClass.OrganizationInfo getOrganizationStructure(String userId, OrganizationOuterClass.OrganizationType returnLevel) throws Exception
    {
        UserOuterClass.User user = _userDal.getUserById(userId);
        user.getOrganizationId();
        return _organizationDal.getOrganizationStructure(user.getOrganizationId(), returnLevel);
    }
}


