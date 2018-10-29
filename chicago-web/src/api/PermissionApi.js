import FetchApi from './FetchApi'

const permission_proto = require('models/permission_pb.js');
const permissionmessages_proto = require('models/permissionmessages_pb.js');

export default class PermissionApi {
  constructor(){
    this.createPermissionsUrl = '/api/permissions/create';
    this.updatePermissionsUrl = '/api/permissions/update';
    this.deletePermissionsUrl = '/api/permissions/delete';
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.fetchApi = new FetchApi();
  }

  createPermission(organizationId, description, errorHandler) {
    let permission = new  permission_proto.Permission();
    permission.setOrganizationId(organizationId);
    permission.setDescription(description);
    return this.permissionCrud(this.createPermissionUrl, permission, permissionmessages_proto.UserPermissionsRequest.deserializeBinary, errorHandler);
  }

  updatePermission(permission, errorHandler) {
    return this.permissionCrud(this.updatePermissionUrl, permission, permissionmessages_proto.UserPermissionsRequest.deserializeBinary, errorHandler);
  }

  deletePermission(organizationId, permission, errorHandler) {
    return this.permissionCrud(this.deletePermissionUrl, permission, permissionmessages_proto.UserPermissionsRequest.deserializeBinary, errorHandler);
  }

  getPermission(organizationId, errorHandler) {
    let permission = new permission_proto.Permission();
 //   permission.setOrganizationId(organizationId);
    return this.permissionCrud(this.getPermissionsUrl, permission, permissionmessages_proto.UserPermissionsRequest.deserializeBinary, errorHandler);
  }

  permissionCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
