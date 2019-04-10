class UiInventory {
  constructor() {
    this.organization_id = "";
    this.inventory_id = "";
    this.inventory_name = "";
    this.description = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryOperation {
  constructor() {
    this.inventory_id = "";
    this.operation_type = 0;
    this.item_id = "";
    this.quantity = 0;
    this.amount = 0.0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryTransfer {
  constructor() {
    this.inventory_from_id = "";
    this.inventory_to_id = 0;
    this.item_id = "";
    this.quantity = 0;
    this.transfer_state = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryPosition {
  constructor() {
    this.inventory_id = "";
    this.item_id = "";
    this.quantity = 0;
    this.amount = 0;
    this.image = null;
    this.description = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryCatalogItem {
  constructor() {
    this.entity_id = "";
    this.item_id = "";
    this.item_category_id = 3;
    this.item_brand_id = 4;
    this.item_unit_id = 5;
    this.item_supplier_id = 6;
    this.description = 7;
    this.weight_net = 0.0;
    this.weight_gross = 0.0;
    this.package_weight = 0.0;
    this.quantity_per_pack = 0;
    this.inbound_quantity = 0;
    this.inbound_unit_id = "";
    this.outbound_quantity = 0;
    this.outbound_unit_id = "";
    this.location_id = "";

    this.ean13 = "";
    this.vendor_code = "";
    this.discontinued = False;
    this.image = null;
    this.certificate = "";
    this.notes = 22;
    this.vendor_price = 0.0;
    this.special_price = 0.0;
    this.retail_price = 0.0;
    this.create_datetime = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryItemCategory {
  constructor() {
    this.entity_id = "";
    this.category_name = "";
    this.category_id = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryItemBrand {
  constructor() {
    this.entity_id = "";
    this.brand_name = "";
    this.brand_id = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryItemUnit {
  constructor() {
    this.entity_id = "";
    this.unit_name = "";
    this.unit_id = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryItemSupplier {
  constructor() {
    this.entity_id = "";
    this.supplier_name = "";
    this.supplier_id = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiInventoryLocation {
  constructor() {
    this.entity_id = "";
    this.supplier_name = "";
    this.supplier_id = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export {UiInventory,
  UiInventoryOperation,
  UiInventoryTransfer,
  UiInventoryPosition,
  UiInventoryCatalogItem,
  UiInventoryItemCategory,
  UiInventoryItemBrand,
  UiInventoryItemUnit,
  UiInventoryItemSupplier,
  UiInventoryLocation};

