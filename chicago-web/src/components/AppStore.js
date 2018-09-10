import {extendObservable} from 'mobx'
const user_proto = require('models/user_pb');

class AppStore {
  constructor() {
    extendObservable(this, {
      permissions: [],
      userData: new user_proto.User()
    });
  }
}

export default AppStore;
