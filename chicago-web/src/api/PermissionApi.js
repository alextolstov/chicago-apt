import FetchApi from './FetchApi'

const permission_proto = require('models/permission_pb.js');
const permissionmessages_proto = require('models/permissionmessages_pb.js');
const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

export default class PermissionApi {
  constructor(){
    this.createPermissionsUrl = '/api/permissions/create';
    this.deletePermissionsUrl = '/api/permissions/delete';
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.getUserRolesUrl = '/api/permissions/get';
    this.saveUserRoleUrl = '/api/permissions/update';
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
    let roles = new user_proto.UserPermissions();
    // пытаюсь получить пользовательские роли 
    console.log('getUserRoles roles=', roles);
    console.log('userId=', user.getUserId());
    roles.setUserId(user.getUserId());

    //  посдставляю в качестве десериализатора - usermessages_proto.SetUserPermissionsResponse.deserializeBinary
    //  после запроса ниже приходит 42 байта - но десериализатор обнаруживает ошибку
    //  можеn что то другое надо подставлять?
   // UserPermissions
    return this.permissionCrud(this.getUserRolesUrl, roles, usermessages_proto.SetUserPermissionsRequest.deserializeBinary, errorHandler);
       
  }

 // user - объект пользователя
 // new roles - масссив id выбранных ролей
  saveUserRoles(user, newRoles, errorHandler) {
    let roles = new user_proto.UserPermissions();
    roles.setUserId(user.getUserId());

    let rolesArr = [];
    let role = [];
    for(let i=0; i<newRoles.length; i++ ) {
      role.push[i];
      role[i] = new permission_proto.Role(); 
     // для пробы записываю только одну роль
      role[i].setRoleId(newRoles[i]);  // у меня сохранен только id выбранной роли - надеюсь этого хватит?
      rolesArr.push(role[i]);          // сохраняю роль в массив
    } 
  
    roles.setRolesList(rolesArr); // массив ролей в объект 
    console.log('mod roles=', roles);
  
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
