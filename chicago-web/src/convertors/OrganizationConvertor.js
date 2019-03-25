import {UiOrganizationInfo, UiOrganization} from "../models/UiOrganization";

class OrganizationConvertor{
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.organization_id = dtoObj.getOrganizationId != undefined ? dtoObj.getOrganizationId() : "";
    uiObj.type = dtoObj.getType != undefined ? dtoObj.getType() : "";
    uiObj.name = dtoObj.getName != undefined ? dtoObj.getName() : "";
    uiObj.description = dtoObj.getDescription != undefined ? dtoObj.getDescription() : "";
    uiObj.web_site = dtoObj.getWebSite != undefined ? dtoObj.getWebSite() : "";
    uiObj.email_domain = dtoObj.getEmailDomain != undefined ? dtoObj.getEmailDomain() : "";
    uiObj.parent_organization_id = dtoObj.getParentOrganizationId != undefined ? dtoObj.getParentOrganizationId() : "";
    uiObj.entity_id = dtoObj.getEntityId != undefined ? dtoObj.getEntityId() : "";
    uiObj.phone = dtoObj.getPhone != undefined ? dtoObj.getPhone() : "";
    uiObj.fax = dtoObj.getFax != undefined ? dtoObj.getFax() : "";
    uiObj.address_id = dtoObj.AddressId != undefined ? dtoObj.AddressId() : "";

    if (dtoObj.getUsersList != undefined) {
      let users = dtoObj.getUsersList();
      if (users.length == 0) {
        return;
      }
      uiObj.users = new Array();
      for (let i = 0; i < users.length; ++i) {
        uiObj.users.push(users[i]);
      }
    }

    if (dtoObj.getOrganizationsList != undefined) {
      let organizations = dtoObj.getOrganizationsList();
      if (organizations.length == 0) {
        return;
      }
      uiObj.organizations = new Array();
      for (let i = 0; i < organizations.length; ++i) {
        uiObj.organizations.push(organizations[i]);
      }
    }
  }

  toDto = (uiObj, dtoObj) =>{
    dtoObj.setOrganizationId(uiObj.organization_id);
    dtoObj.setType(uiObj.type);
    dtoObj.setName(uiObj.name);
    dtoObj.setDescription(uiObj.description);
    dtoObj.setWebSite(uiObj.web_site);
    dtoObj.setEmailDomain(uiObj.email_domain);
    dtoObj.setParentOrganizationId(uiObj.parent_organization_id);
    dtoObj.setEntityId(uiObj.entity_id);
    dtoObj.setPhone(uiObj.phone);
    dtoObj.setFax(uiObj.fax);
  }
}

export default OrganizationConvertor;
