import FetchApi from './FetchApi'

const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

export default class UserApi {
  constructor(){
    this.testAuthUrl = '/api/login/testauth';
    this.loginUrl = '/login';
    this.logoutUrl = '/logout';
    this.getUserUrl = '/api/users/user';
    this.createUserUrl = '/api/users/create';
    this.saveUserUrl = '/api/users/saveuser';
    this.fetchApi = new FetchApi();
  }

  testAuth() {
    return fetch(this.testAuthUrl, {
      method: "GET",
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
      method: "POST",
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
        return this.getUserByEmail(userName, errorHandler).then(function (data) {
          return data
        });
      })
      .catch(rest_error => {
        if (rest_error.status == 401 && errorHandler !== null) {
          errorHandler("User unauthorized");
        } else if (rest_error.status == 404 && errorHandler !== null) {
          errorHandler("Error 404. Page not found.");
        } else if (rest_error.status == 500 && errorHandler !== null) {
          errorHandler("Error 500. Server error.");
        } else {
          rest_error.json().then(errorMessage => {
            if (errorHandler !== null) {
              errorHandler(errorMessage);
            }
          })
        }
      })
  }

  logout(successHandler, errorHandler) {
    fetch(this.logoutUrl, {
      method: "GET",
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
        })
      })
  }

  getUserById(user_id, errorHandler) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setUserId(user_id);
    return this.getUser(user, errorHandler);
  }

  getUserByEmail(email, errorHandler) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setEmail(email.toLowerCase());
    return this.getUser(user, errorHandler);
  }

  getUser(user, errorHandler) {
    return this.userCrud(this.getUserUrl, user);
  }

  createUser(user, errorHandler) {
    return this.userCrud(this.createUserUrl, user);
  }

  saveUser(user, errorHandler) {
    return this.userCrud(this.saveUserUrl, user, errorHandler);
  }

  userCrud(url, userObject, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      usermessages_proto.UserResponse.deserializeBinary,
      errorHandler);
  }
}
