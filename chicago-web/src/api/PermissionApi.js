import FetchApi from './FetchApi'

const permission_proto = require('models/permission_pb.js');
const permissionmessages_proto = require('models/permissionmessages_pb.js');

export default class PermissionApi {
  constructor(){
    this.createPermissionsUrl = '/api/permissions/create';
    this.updatePermissionsUrl = '/api/permissions/update';
    this.deletePermissionsUrl = '/api/permissions/delete';
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.getUserRolesUrl = '/api/permissions/get';
    this.fetchApi = new FetchApi();
  }

  createPermission(organizationId, description, errorHandler) {
    let permission = new  permission_proto.Permission();
    permission.setOrganizationId(organizationId);
    permission.setDescription(description);
    return this.permissionCrud(this.createPermissionUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  }

  updatePermission(permission, errorHandler) {
    return this.permissionCrud(this.updatePermissionUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  }

  deletePermission(organizationId, permission, errorHandler) {
    return this.permissionCrud(this.deletePermissionUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  }

  getPermission(errorHandler) {
    let permission = new permission_proto.Permission();
    return this.permissionCrud(this.getPermissionsUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  }
  getUserRoles(user, errorHandler) {
    let permission = new permission_proto.Permission();
    let role = new permission_proto.Role();
    let roles = new permission_proto.Roles();
  /*  
    return this.permissionCrud(this.getUserRolesUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
  */
   console.log('getUserRoles pemission=', permission);
   console.log('getUserRoles role=', role);
   console.log('getUserRoles roles=', roles);
   console.log('userId=', user.getUserId());
   permission.setPermissionId(user.getUserId());
   return this.permissionCrud(this.getUserRolesUrl, permission, permissionmessages_proto.SystemPermissionsResponse.deserializeBinary, errorHandler);
       
  }

  permissionCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
