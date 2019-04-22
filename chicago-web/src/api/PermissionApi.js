import FetchApi from './FetchApi'
import {UiPermission, UiRole} from "../models/UiPermission";
import {PermissionConvertor, RoleConvertor} from "../convertors/PermissionConvertor";

const permission_proto = require('dto/permission_pb.js');
const permissionmessages_proto = require('dto/permissionmessages_pb.js');
const user_proto = require('dto/user_pb');
const usermessages_proto = require('dto/usermessages_pb.js');

export default class PermissionApi {
  constructor() {
    this.getSystemRolesUrl = '/api/permissions/getsystemroles';
    this.getUserRolesUrl = '/api/permissions/get';
    this.saveUserRoleUrl = '/api/permissions/update';
    this.fetchApi = new FetchApi();
    this.perm_convertor = new PermissionConvertor();
    this.role_convertor = new RoleConvertor();
  }

  // Global system roles
  getSystemRoles(errorHandler) {
    let permission = new permission_proto.Permission();
    let self = this;
    return this.fetchApi.restCrud(this.getSystemRolesUrl, permission, permissionmessages_proto.SystemRolesResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiRoles(self, msg);
      });
  }

  getUserRoles(userId, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(userId);
    let self = this;
    return this.fetchApi.restCrud(this.getUserRolesUrl, roles, permissionmessages_proto.UserPermissionsResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        let perm = msg.getPermissions();
        return self.getUiRoles(self, perm);
      });
  }

  setUserRoles(userId, newRoles, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(userId);

    let rolesArr = [];
    let role = [];
    for (let i = 0; i < newRoles.length; i++) {
      role.push(i);
      role[i] = new permission_proto.Role();
      role[i].setRoleId(newRoles[i]);
      rolesArr.push(role[i]);
    }

    roles.setRolesList(rolesArr);
    return this.fetchApi.restCrud(this.saveUserRoleUrl, roles, usermessages_proto.SetUserPermissionsResponse.deserializeBinary, errorHandler);
  }

  // get user Permissions and save to appStore
  setUserPermissions(appStore, userId, callback) {
    let self = this;
    self.getSystemRoles(null)
      .then(function (sysRoles) {
        if (sysRoles !== undefined && sysRoles !== null) {
          appStore.companyRoles = new Array();
          appStore.userPermissions = new Array();

          sysRoles.forEach((item, i) => {
            const v = item.role_id;
            const l = item.role_name;
            appStore.companyRoles.push({value: v, label: l});
          });

          self.getUserRoles(userId, null)
            .then(function (userRoles) {
              if (userRoles) {
                for(let i = 0; i < userRoles.length; i++) {
                  for(let j = 0; j < userRoles[i].permissions.length; j++) {
                    let permName = userRoles[i].permissions[j].permission_name;
                    if(!appStore.userPermissions.find((elm) => { return elm === permName})) {
                      appStore.userPermissions.push(permName);
                    }
                  }
                }
              }
              // Redirect current page to dashboard
              callback();
            })
        }
      });
  }

  getUiPermissions(self, msg) {
    let savedPermissions = msg.getPermissionsList();
    let uiPermissions = [];

    if (savedPermissions != null) {
      for (let i = 0; i < savedPermissions.length; i++) {
        let uiPermission = new UiPermission()
        self.perm_convertor.fromDto(savedPermissions[i], uiPermission);
        uiPermissions.push(uiPermission);
      }
    }
    return Promise.resolve(uiPermissions);
  }

  getUiRoles(self, msg) {
    let savedRoles = msg.getRolesList();
    let uiRoles = [];

    if (savedRoles != null) {
      for (let i = 0; i < savedRoles.length; i++) {
        let uiRole = new UiRole();
        self.role_convertor.fromDto(savedRoles[i], uiRole);
        uiRoles.push(uiRole);
      }
    }
    return Promise.resolve(uiRoles);
  }
}
