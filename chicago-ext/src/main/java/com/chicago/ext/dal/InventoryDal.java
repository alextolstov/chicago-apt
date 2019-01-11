package com.chicago.ext.dal;

import com.chicago.dto.OrganizationOuterClass;

public interface InventoryDal
{
    String createInventory(String organizationId) throws Exception;

    void updateInventory(OrganizationOuterClass.Organization organization);
}
