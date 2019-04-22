import {UiPermission} from "../models/UiPermission";

class PermissionConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.permission_id = dtoObj.getPermissionId != undefined ? dtoObj.getPermissionId() : "";
    uiObj.permission_name = dtoObj.getPermissionName != undefined ? dtoObj.getPermissionName() : "";
    uiObj.description = dtoObj.getDescription != undefined ? dtoObj.getDescription() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setPermissionId(uiObj.permission_id);
    dtoObj.setPermissionName(uiObj.permission_name);
    dtoObj.setDescription(uiObj.description);
  }
}

class RoleConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.role_id = dtoObj.getRoleId != undefined ? dtoObj.getRoleId() : "";
    uiObj.role_name = dtoObj.getRoleName != undefined ? dtoObj.getRoleName() : "";
    uiObj.role_description = dtoObj.getRoleDescription != undefined ? dtoObj.getRoleDescription() : "";

    let protoPermissions = dtoObj.getPermissionsList != undefined ? dtoObj.getPermissionsList() : [];
    for(let i = 0; i < protoPermissions.length; i++) {
      let uiPerm = new UiPermission();
      uiPerm.permission_id = protoPermissions[i].getPermissionId();
      uiPerm.permission_name = protoPermissions[i].getPermissionName();
      uiPerm.description = protoPermissions[i].getDescription();
      uiObj.permissions.push(uiPerm);
    }
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setRoleId(uiObj.role_id);
    dtoObj.setRoleName(uiObj.role_name);
    dtoObj.setRoleDescription(uiObj.role_description);
    dtoObj.setPermissionsList(uiObj.permissions);
  }
}

export {PermissionConvertor, RoleConvertor};
