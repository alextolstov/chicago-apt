import ManageUsers from "../views/Users/ManageUsers";

class UiOrganizationInfo {
  constructor() {
    this.type = "";
    this.organization_id = "";
    this.name = "";
    this.children = null;
  }

}

class UiOrganization {
  constructor() {
    this.organization_id = "";
    this.type = "";
    this.name = "";
    this.description = "";
    this.web_site = "";
    this.email_domain = "";
    this.parent_organization_id = "";
    this.entity_id = "";
    this.phone = "";
    this.fax = "";
    this.users = [];
    this.organizations = [];
    this.address_id = "";
  }
}

export {UiOrganizationInfo, UiOrganization};
