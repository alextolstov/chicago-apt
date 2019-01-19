package com.chicago.ext.dal;

import com.chicago.dto.Inventory;
import com.chicago.dto.OrganizationOuterClass;

public interface InventoryDal
{
    String createInventory(String organizationId) throws Exception;

    void updateInventory(OrganizationOuterClass.Organization organization);

    Inventory.InventoryItem createInventoryItem(Inventory.InventoryItem inventoryItem);

    void updateInventoryItem(Inventory.InventoryItem inventoryItem);

    void applyInventoryOperation(Inventory.InventoryOperation inventoryOperation);

    void startInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);

    void acceptInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);

    void rejectInventoryTransfer(Inventory.InventoryTransfer inventoryTransfer);
}
