import {UiPermission} from "../models/UiPermission";

class PermissionConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.permission_id = dtoObj.getPermissionId != undefined ? dtoObj.getPermissionId() : "";
    uiObj.permission_name = dtoObj.getPermissionName != undefined ? dtoObj.getPermissionName() : "";
    uiObj.permission_description = dtoObj.getPermissionDescription != undefined ? dtoObj.getPermissionDescription() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setPermissionId(uiObj.permission_id);
    dtoObj.setPermissionName(uiObj.permission_name);
    dtoObj.setPermissionDescription(uiObj.permission_description);
  }
}

class RoleConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.role_id = dtoObj.getRoleId != undefined ? dtoObj.getRoleId() : "";
    uiObj.role_name = dtoObj.getRoleName != undefined ? dtoObj.getRoleName() : "";
    uiObj.role_description = dtoObj.getRoleDescription != undefined ? dtoObj.getRoleDescription() : "";
    uiObj.permissions = dtoObj.getPermissions != undefined ? dtoObj.getPermissions() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setRoleId(uiObj.role_id);
    dtoObj.setRoleName(uiObj.role_name);
    dtoObj.setRoleDescription(uiObj.role_description);
    dtoObj.setPermissions(uiObj.permissions);
  }
}

export {PermissionConvertor, RoleConvertor};
