import FetchApi from './FetchApi'
import _ from 'lodash';

const permission_proto = require('dto/permission_pb.js');
const permissionmessages_proto = require('dto/permissionmessages_pb.js');
const user_proto = require('dto/user_pb');
const usermessages_proto = require('dto/usermessages_pb.js');

export default class PermissionApi {
  constructor(){
    this.getPermissionsUrl = '/api/permissions/getsystem';
    this.getUserRolesUrl = '/api/permissions/get';
    this.saveUserRoleUrl = '/api/permissions/update';
    this.fetchApi = new FetchApi();
    this.permissionsArr = [];
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
      role.push(i);
      role[i] = new permission_proto.Role();
      role[i].setRoleId(newRoles[i]);  
      rolesArr.push(role[i]);          
    }
  
    roles.setRolesList(rolesArr); 
    return this.permissionCrud(this.saveUserRoleUrl, roles, usermessages_proto.SetUserPermissionsResponse.deserializeBinary, errorHandler);

  }
 // get user Permissions and save to appStore
  setPermissionsUser(appStore,user, callback) {
       let self = this;
       self.getPermission(null)
          .then(function (data) {
            if(data!==undefined&&data!==null) {
              appStore.companyPermissions=[];
              appStore.userPermissions=[];
              let roles=data.getRoles();
              let rolesList=roles.getRoleList();
              rolesList.forEach((item, i) => {
                const v=item.getRoleId();
                const l=item.getRoleName();
                appStore.companyPermissions.push({value: v, label: l});
                self.permissionsArr.push({value: v, label: l});

              });
              self.getUserRoles(user, null)
                .then(function (data) {
                  if(data) {
                    let userPermissions=data.getPermissions();
                    let rolesList=userPermissions.getRolesList();

                    rolesList.forEach((item, i) => {
                      const v=item.getRoleId();
                      const lobj=_.find(self.permissionsArr, {value: v});
                      const l=lobj.label;
                      appStore.userPermissions.push({value: v, label: l});
                    });
                  }
                  // Redirect current page to dashboard
                  callback();
                })

            }
          });
  }        

  permissionCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
