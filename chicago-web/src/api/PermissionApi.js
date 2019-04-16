import FetchApi from './FetchApi'
import _ from 'lodash';
import {UiPermission, UiRole} from "../models/UiPermission";
import {PermissionConvertor, RoleConvertor} from "../convertors/PermissionConvertor";

const permission_proto = require('dto/permission_pb.js');
const permissionmessages_proto = require('dto/permissionmessages_pb.js');
const user_proto = require('dto/user_pb');
const usermessages_proto = require('dto/usermessages_pb.js');

export default class PermissionApi {
  constructor() {
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.getUserRolesUrl = '/api/permissions/get';
    this.saveUserRoleUrl = '/api/permissions/update';
    this.fetchApi = new FetchApi();
    this.permissionsArr = [];
    this.perm_convertor = new PermissionConvertor();
    this.role_convertor = new RoleConvertor();
  }

  getPermission(errorHandler) {
    let permission = new permission_proto.Permission();
    let self = this;
    return this.fetchApi.restCrud(this.getPermissionsUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPermission(self, msg);
      });
  }

  getUserRoles(userId, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(userId);
    let self = this;
    return this.fetchApi.restCrud(this.getUserRolesUrl, roles, permissionmessages_proto.UserPermissionsResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiRoles(self, msg);
      });
  }

  saveUserRoles(userId, newRoles, errorHandler) {
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
  setPermissionsUser(appStore, userId, callback) {
    let self = this;
    self.getPermission(null)
      .then(function (data) {
        if (data !== undefined && data !== null) {
          appStore.companyPermissions = [];
          appStore.userPermissions = [];
          let roles = data.getRoles();
          let rolesList = roles.getRoleList();
          rolesList.forEach((item, i) => {
            const v = item.getRoleId();
            const l = item.getRoleName();
            appStore.companyPermissions.push({value: v, label: l});
            self.permissionsArr.push({value: v, label: l});

          });
          self.getUserRoles(userId, null)
            .then(function (data) {
              if (data) {
                let userPermissions = data.getPermissions();
                let rolesList = userPermissions.getRolesList();

                rolesList.forEach((item, i) => {
                  const v = item.getRoleId();
                  const lobj = _.find(self.permissionsArr, {value: v});
                  const l = lobj.label;
                  appStore.userPermissions.push({value: v, label: l});
                });
              }
              // Redirect current page to dashboard
              callback();
            })

        }
      });
  }

  getUiPermission(self, msg) {
    let savedPermission = msg.getPermission();
    let uiPermission = null;
    if (savedPermission != null) {
      uiPermission = new UiPermission();
      self.perm_convertor.fromDto(savedPermission, uiPermission);
    }
    return Promise.resolve(uiPermission);
  }

  getUiRoles(self, msg) {
    let savedRoles = msg.getRoles();
    let uiRoles = new Array();
    if (savedRoles != null) {
      for(let i = 0; i < savedRoles.length; i++) {
        let uiRole = new UiRole();
        self.role_convertor.fromDto(savedRoles[i], uiRole);
        uiRoles.push(uiRole);
      }
    }
    return Promise.resolve(uiRoles);
  }
}
