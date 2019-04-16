class PositionConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.organization_id = dtoObj.getOrganizationId != undefined ? dtoObj.getOrganizationId() : "";
    uiObj.position_id = dtoObj.getPositionId != undefined ? dtoObj.getPositionId() : "";
    uiObj.description = dtoObj.getDescription != undefined ? dtoObj.getDescription() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setOrganizationId(uiObj.organization_id);
    dtoObj.setPositionId(uiObj.position_id);
    dtoObj.setDescription(uiObj.description);
  }
}

export default PositionConvertor;
