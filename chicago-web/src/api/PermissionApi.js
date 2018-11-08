import FetchApi from './FetchApi'

const permission_proto = require('models/permission_pb.js');
const permissionmessages_proto = require('models/permissionmessages_pb.js');
const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

export default class PermissionApi {
  constructor(){
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.getUserRolesUrl = '/api/permissions/get';
    this.saveUserRoleUrl = '/api/permissions/update';
    this.fetchApi = new FetchApi();
  }

  getPermission(errorHandler) {
    let permission = new permission_proto.Permission();
    return this.permissionCrud(this.getPermissionsUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  }
  
  getUserRoles(user, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(user.getUserId());
    return this.permissionCrud(this.getUserRolesUrl, roles, permissionmessages_proto.UserPermissionsResponse.deserializeBinary, errorHandler);
       
  }

  saveUserRoles(user, newRoles, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(user.getUserId());

    let rolesArr = [];
    let role = [];
    for(let i=0; i<newRoles.length; i++ ) {
      role.push[i];
      role[i] = new permission_proto.Role();
      role[i].setRoleId(newRoles[i]);  
      rolesArr.push(role[i]);          
    }
  
    roles.setRolesList(rolesArr); 
    return this.permissionCrud(this.saveUserRoleUrl, roles, usermessages_proto.SetUserPermissionsResponse.deserializeBinary, errorHandler);

  }

  permissionCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
