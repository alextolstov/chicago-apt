import FetchApi from './FetchApi'

const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

export default class UserApi {
  constructor(){
    this.testAuthUrl = '/api/login/testauth';
    this.loginUrl = '/login';
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

  login(credentials, userName, context) {
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
        return this.getUserByEmail(userName, context).then(function (data) {
          return data
        });
      })
      .catch(rest_error => {
        if (rest_error.status == 401) {
          context.handleError("User unauthorized");
        } else if (rest_error.status == 404) {
          context.handleError("Error 404. Page not found.");
        } else if (rest_error.status == 500) {
          context.handleError("Error 500. Server error.");
        } else {
          rest_error.json().then(errorMessage => {
            context.handleError(errorMessage);
          })
        }
      })
  }

  getUserById(user_id, context) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setUserId(user_id);
    return this.getUser(user, context);
  }

  getUserByEmail(email, context) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setEmail(email.toLowerCase());
    return this.getUser(user, context);
  }

  getUser(user, context) {
    return this.userCrud(this.getUserUrl, user);
  }

  createUser(user, context) {
    return this.userCrud(this.createUserUrl, user);
  }

  saveUser(user, context) {
    return this.userCrud(this.saveUserUrl, user);
  }

  userCrud(url, userObject, context) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      usermessages_proto.UserResponse.deserializeBinary,
      context);
  }
}
