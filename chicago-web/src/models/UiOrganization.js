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

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiOrganization;
