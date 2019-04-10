class UiOrganizationInfo {
  constructor() {
    this.type = "";
    this.organization_id = "";
    this.name = "";
    this.children = null;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiOrganizationInfo;
