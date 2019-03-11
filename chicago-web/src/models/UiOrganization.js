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

  getCopy() {
    let newObj = new UiOrganization();
    newObj.organization_id = this.organization_id;
    newObj.type = this.type;
    newObj.name = this.name;
    newObj.description = this.description;
    newObj.web_site = this.web_site;
    newObj.email_domain = this.email_domain;
    newObj.parent_organization_id = this.parent_organization_id;
    newObj.entity_id = this.entity_id;
    newObj.phone = this.phone;
    newObj.fax = this.fax;
    newObj.users = this.users;
    newObj.organizations = this.organizations;
    newObj.address_id = this.address_id;
  }
}

export default UiOrganization;
