import {UiInventory, UiInventoryOperation} from "../models/UiInventory";

class InventoryConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.organization_id = dtoObj.getOrganizationId != undefined ? dtoObj.getOrganizationId() : "";
    this.inventory_id = dtoObj.getInventoryId != undefined ? dtoObj.getInventoryId() : "";
    this.inventory_name = dtoObj.getInventoryName != undefined ? dtoObj.getInventoryName() : "";
    this.description = dtoObj.getDescription != undefined ? dtoObj.getDescription() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setOrganizationId(uiObj.organization_id);
    dtoObj.setInventoryId(uiObj.inventory_id);
    dtoObj.setInventoryName(uiObj.inventory_name);
    dtoObj.setDescription(uiObj.description);
  }
}

class InventoryOperationConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.inventory_id = dtoObj.getInventoryId != undefined ? dtoObj.getInventoryId() : "";
    this.operation_type = dtoObj.getOperationType != undefined ? dtoObj.getOperationType() : 0;
    this.item_id = dtoObj.getItemId != undefined ? dtoObj.getItemId() : 0;
    this.quantity = dtoObj.getQuantity != undefined ? dtoObj.getQuantity() : 0;
    this.amount = dtoObj.getAmount != undefined ? dtoObj.getAmount() : 0;
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setInventoryId(uiObj.inventory_id);
    dtoObj.setOperationType(uiObj.operation_type);
    dtoObj.setItemId(uiObj.item_id);
    dtoObj.setQuantity(uiObj.quantity);
    dtoObj.setAmount(uiObj.amount);
  }
}

class InventoryTransferConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.inventory_from_id = dtoObj.getInventoryFromId != undefined ? dtoObj.getInventoryFromId() : "";
    this.inventory_to_id = dtoObj.getInventoryToId != undefined ? dtoObj.getInventoryToId() : 0;
    this.item_id = dtoObj.getItemId != undefined ? dtoObj.getItemId() : 0;
    this.quantity = dtoObj.getQuantity != undefined ? dtoObj.getQuantity() : 0;
    this.amount = dtoObj.getAmount != undefined ? dtoObj.getAmount() : 0;
    this.transfer_state = dtoObj.getTransferState != undefined ? dtoObj.getTransferState() : 0;
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setInventoryFromId(uiObj.inventory_from_id);
    dtoObj.setInventoryToId(uiObj.inventory_to_id);
    dtoObj.setItemId(uiObj.item_id);
    dtoObj.setQuantity(uiObj.quantity);
    dtoObj.setAmount(uiObj.amount);
    dtoObj.setTransferState(uiObj.transfer_state);
  }
}

class InventoryPositionConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.inventory_id = dtoObj.getInventoryId != undefined ? dtoObj.getInventoryId() : "";
    this.item_id = dtoObj.getItemId != undefined ? dtoObj.getItemId() : 0;
    this.quantity = dtoObj.getQuantity != undefined ? dtoObj.getQuantity() : 0;
    this.amount = dtoObj.getAmount != undefined ? dtoObj.getAmount() : 0;
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setInventoryId(uiObj.inventory_id);
    dtoObj.setItemId(uiObj.item_id);
    dtoObj.setQuantity(uiObj.quantity);
    dtoObj.setAmount(uiObj.amount);
  }
}

class InventoryCatalogItemConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.item_id = dtoObj.getItemId != undefined ? dtoObj.getItemId() : "";
    this.item_category_id = dtoObj.getItemCategoryId != undefined ? dtoObj.getItemCategoryId() : "";
    this.item_brand_id = dtoObj.getItemBrandId != undefined ? dtoObj.getItemBrandId() : "";
    this.item_unit_id = dtoObj.getItemUnitId != undefined ? dtoObj.getItemUnitId() : ""
    this.item_supplier_id = dtoObj.getItemSupplierId != undefined ? dtoObj.getItemSupplierId() : "";
    this.description = dtoObj.getDescription != undefined ? dtoObj.getDescription() : "";
    this.weight_net = dtoObj.getWeightNet != undefined ? dtoObj.getWeightNet() : 0;
    this.weight_gross = dtoObj.getWeightGross != undefined ? dtoObj.getWeightGross() : 0;
    this.package_weight = dtoObj.getPackageWeight != undefined ? dtoObj.getPackageWeight() : 0;
    this.quantity_per_pack = dtoObj.getQuantityPerPack != undefined ? dtoObj.getQuantityPerPack() : 0;
    this.inbound_quantity = dtoObj.getInboundQuanitity != undefined ? dtoObj.getInboundQuanitity() : 0;
    this.inbound_unit_id = dtoObj.getInboundUnitId != undefined ? dtoObj.getInboundUnitId() : "";
    this.outbound_quantity = dtoObj.getOutboundQuanitity != undefined ? dtoObj.getOutboundQuanitity() : 0;
    this.outbound_unit_id = dtoObj.getOutboundUnitId != undefined ? dtoObj.getOutboundUnitId() : "";
    this.location_id = dtoObj.getLocationid != undefined ? dtoObj.getLocationid() : "";
    this.ean13 = dtoObj.getEan13 != undefined ? dtoObj.getEan13() : "";
    this.vendor_code = dtoObj.getVendorCode != undefined ? dtoObj.getVendorCode() : "";
    this.discontinued  = dtoObj.getDiscontinued != undefined ? dtoObj.getDiscontinued() : false;
    this.image  = dtoObj.getImage != undefined ? dtoObj.getImage() : null;
    this.certificate  = dtoObj.getCertificate != undefined ? dtoObj.getCetificate() : "";
    this.notes = dtoObj.getNotes != undefined ? dtoObj.getNotes() : "";
    this.vendor_price = dtoObj.getVendorPrice != undefined ? dtoObj.getVendorPrice() : "";
    this.special_price = dtoObj.getSpecialPrice != undefined ? dtoObj.getSpecialPrice() : "";
    this.retail_price = dtoObj.getRetailPrice != undefined ? dtoObj.getRetailPrice() : "";
    this.create_datetime = dtoObj.getCreateDateTime != undefined ? dtoObj.getCreateDateTime() : 0;
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setItemId(uiObj.item_id);
    dtoObj.setItemCategoryId(uiObj.item_category_id);
    dtoObj.setItemBrandId(uiObj.item_brand_id);
    dtoObj.setItemUnitId(uiObj.item_unit_id);
    dtoObj.setItemSupplierId(uiObj.item_supplier_id);
    dtoObj.setDescription(uiObj.description);
    dtoObj.setWeightNet(uiObj.weight_net);
    dtoObj.setWeightGross(uiObj.weight_gross);
    dtoObj.setPackageWeight(uiObj.package_weight);
    dtoObj.setQuantityPerPack(uiObj.quantity_per_pack);
    dtoObj.setInboundUnitId(uiObj.inbound_unit_id);
    dtoObj.setOutboundUnitId(uiObj.outbound_unit_id);
    dtoObj.setOutboundQuantity(uiObj.outbound_quantity);
    dtoObj.setOutboundUnitId(uiObj.outbound_unit_id);
    dtoObj.setLocationId(uiObj.location_id);
    dtoObj.setEan13(uiObj.ean13);
    dtoObj.setVendorCode(uiObj.vendor_code);
    dtoObj.setDiscontinued(uiObj.discontinued);
    dtoObj.setImage(uiObj.image);
    dtoObj.setCertificate(uiObj.certificate);
    dtoObj.setVendorPrice(uiObj.vendor_price);
    dtoObj.setSpecialPrice(uiObj.special_price);
    dtoObj.setRetailPrice(uiObj.retail_price);
    dtoObj.setCreateDatetime(uiObj.create_datetime);
  }
}

class InventoryItemCategoryConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.category_name = dtoObj.getCategoryName != undefined ? dtoObj.getCategoryName() : "";
    this.category_id = dtoObj.getCategoryId != undefined ? dtoObj.getCategoryId() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setCategoryName(uiObj.category_name);
    dtoObj.setCategoryId(uiObj.category_id);
  }
}

class InventoryItemBrandConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.brand_name = dtoObj.getBrandName != undefined ? dtoObj.getBrandName() : "";
    this.brand_id = dtoObj.getBrandId != undefined ? dtoObj.getBrandId() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setBrandName(uiObj.brand_name);
    dtoObj.setBrandId(uiObj.brand_id);
  }
}

class InventoryItemUnitConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.unit_name = dtoObj.getUnitName != undefined ? dtoObj.getUnitName() : "";
    this.unit_id = dtoObj.getUnitId != undefined ? dtoObj.getUnitId() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setUnitName(uiObj.brand_name);
    dtoObj.setUnitId(uiObj.brand_id);
  }
}

class InventoryItemSupplierConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.supplier_name = dtoObj.getSupplierName != undefined ? dtoObj.getSupplierName() : "";
    this.supplier_id = dtoObj.getSupplierId != undefined ? dtoObj.getSupplierId() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setUnitName(uiObj.brand_name);
    dtoObj.setUnitId(uiObj.brand_id);
  }
}

class InventoryItemLocationConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    this.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    this.location_name = dtoObj.getLocationName != undefined ? dtoObj.getLocationName() : "";
    this.location_id = dtoObj.getLocationId != undefined ? dtoObj.getLocationId() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setLocationName(uiObj.location_name);
    dtoObj.setLocationId(uiObj.location_id);
  }
}

export {InventoryConvertor,
  InventoryOperationConvertor,
  InventoryTransferConvertor,
  InventoryPositionConvertor,
  InventoryCatalogItemConvertor,
  InventoryItemCategoryConvertor,
  InventoryItemBrandConvertor,
  InventoryItemUnitConvertor,
  InventoryItemSupplierConvertor,
  InventoryItemLocationConvertor};
