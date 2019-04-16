import FetchApi from './FetchApi';
import UiUser from "../models/UiUser";
import UserConvertor from "../convertors/UserConvertor";

const user_proto = require('dto/user_pb');
const usermessages_proto = require('dto/usermessages_pb.js');

export default class UserApi {
  constructor() {
    this.testAuthUrl = '/api/login/testauth';
    this.loginUrl = '/login';
    this.logoutUrl = '/logout';
    this.getUserUrl = '/api/users/user';
    this.createUserUrl = '/api/users/create';
    this.saveUserUrl = '/api/users/saveuser';
    this.getUsersUrl = '/api/users/getusers';
    this.fetchApi = new FetchApi();
    this.convertor = new UserConvertor();
  }

  testAuth() {
    return fetch(this.testAuthUrl, {
      method: 'GET',
      credentials: 'include'
    }).then(response => {
      if (!response.ok) {
        return null;
      }
      return response.text();
    }).then(user_id => {
      return user_id;
    });
  }

  login(credentials, userName, errorHandler) {
    let self = this;
    return fetch(this.loginUrl, {
      method: 'POST',
      body: credentials,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        return this.getUserByPhoneOrEmail(userName, errorHandler)
          .then(function (msg) {
            return self.getUiUser(self, msg);
        });
      })
      .catch(rest_error => {
        if (rest_error.status === 401 && errorHandler !== null) {
          errorHandler('User unauthorized');
        } else if (rest_error.status === 404 && errorHandler !== null) {
          errorHandler('User not found or password is invalid');
        } else if (rest_error.status === 500 && errorHandler !== null) {
          errorHandler('Server error');
        } else {
          rest_error.json().then(errorMessage => {
            if (errorHandler !== null) {
              errorHandler(errorMessage);
            }
          });
        }
      });
  }

  logout(successHandler, errorHandler) {
    fetch(this.logoutUrl, {
      method: 'GET',
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        successHandler();
      })
      .catch(rest_error => {
        rest_error.json().then(errorMessage => {
          if (errorHandler !== null) {
            errorHandler(errorMessage);
          }
        });
      });
  }

  getUserById(user_id, errorHandler) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setUserId(user_id);
    return this.getUser(user, errorHandler);
  }

  getUserByPhoneOrEmail(phoneOrEmail, errorHandler) {
    let user = new user_proto.User();
    // Check if email as username. Must be lower case
    if (phoneOrEmail.search('@') !== -1) {
      user.setEmail(phoneOrEmail.toLowerCase());
    }
    else {
      user.setCellPhone(phoneOrEmail.toLowerCase());
    }
    return this.getUser(user, errorHandler);
  }

  getUser(user, errorHandler) {
    let self = this;
    return this.fetchApi.restCrud(this.getUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler)
    .then(function (msg) {
      return self.getUiUser(self, msg);
    });
  }

  getUsers(userOrganization, errorHandler) {
    let self = this;
    return this.fetchApi.restCrud(this.getUsersUrl, userOrganization, usermessages_proto.GetUsersResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiUsers(self, msg);
      });
  }

  createUser(user, errorHandler) {
    let self = this;
    return this.fetchApi.restCrud(this.createUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiUser(self, msg);
      });
  }

  saveUser(user, errorHandler) {
    return this.fetchApi.restCrud(this.saveUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler);
  }

  getUiUsers(self, msg) {
    let savedUsers = msg.getUsers();
    let uiUsers = new Array();
    if (savedUsers != null) {
      for(let i = 0; i < savedUsers.length; i++) {
        let uiUser = new UiUser();
        self.convertor.fromDto(savedUsers[i], uiUser);
        uiUsers.push(uiUser);
      }
    }
    return Promise.resolve(uiUsers);
  }

  getUiUser(self, msg) {
    let savedUser = msg.getUser();
    let uiUser = null;
    if (savedUser != null) {
      uiUser = new UiUser();
      self.convertor.fromDto(savedUser, uiUser);
    }
    return Promise.resolve(uiUser);
  }

}
