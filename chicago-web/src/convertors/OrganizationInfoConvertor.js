import UiOrganizationInfo from "../models/UiOrganizationInfo";

class OrganizationInfoConvertor{
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.type = dtoObj.getType != undefined ? dtoObj.getType() : "";
    uiObj.organization_id = dtoObj.getOrganizationId != undefined ? dtoObj.getOrganizationId() : "";
    uiObj.name = dtoObj.getName != undefined ? dtoObj.getName() : "";

    if (dtoObj.getOrganizationsList != undefined) {
      let orgs = dtoObj.getOrganizationsList();
      if (orgs.length == 0) {
        return;
      }
      uiObj.children = new Array();
      for (let i = 0; i < orgs.length; ++i) {
        let nextOrg = new UiOrganizationInfo();
        this.fromDto(orgs[i], nextOrg);
        uiObj.children.push(nextOrg);
      }
    }
  }

  toDto = (uiObj, dtoObj) =>{

  }
}

export default OrganizationInfoConvertor;
