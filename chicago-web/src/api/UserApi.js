import FetchApi from './FetchApi';

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
        return this.getUserByPhoneOrEmail(userName, errorHandler).then(function (data) {
          return data;
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
    return this.userCrud(this.getUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler);
  }

  getUsers(userOrganization, errorHandler) {
    return this.userCrud(this.getUsersUrl, userOrganization, usermessages_proto.GetUsersResponse.deserializeBinary, errorHandler);
  }

  createUser(user, errorHandler) {
    return this.userCrud(this.createUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler);
  }

  saveUser(user, errorHandler) {
    return this.userCrud(this.saveUserUrl, user, usermessages_proto.UserResponse.deserializeBinary, errorHandler);
  }

  userCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
