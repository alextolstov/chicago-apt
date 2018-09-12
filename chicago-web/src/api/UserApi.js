const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

export default class UserApi {
  testAuth() {
    return fetch('/api/login/testauth', {
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
    return fetch('/login', {
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
    let serialized_user = user.serializeBinary();
    return this.getUser(serialized_user, context);
  }

  getUserByEmail(email, context) {
    let user = new user_proto.User();
    // Username must be lower case
    user.setEmail(email.toLowerCase());
    let serialized_user = user.serializeBinary();
    return this.getUser(serialized_user, context);
  }

  getUser(serialized_user, context) {
    return fetch('/api/users/user', {
      method: "POST",
      body: serialized_user,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.arrayBuffer();
    }).then(proto => {
      let user_response = usermessages_proto.UserResponse.deserializeBinary(proto);
      if (user_response.getTransactionError() !== undefined) {
        context.handleError(user_response.getTransactionError().getErrorMessage());
      } else {
        return user_response.getUser();
      }
    }).catch(rest_error => {
      if (rest_error.status == 500) {
        // Show error
        context.handleError("Error 500. Server error.");
        return;
      }
      rest_error.json().then(errorMessage => {
        context.handleError(errorMessage);
      })
    })
  }
}
