import {extendObservable} from 'mobx'
const user_proto = require('dto/user_pb');

class AppStore {
  constructor() {
    extendObservable(this, {
      userPermissions: [],
      userPositions: [],
      companyPositions: [],
      userData: new user_proto.User()
    });
  }
}

export default AppStore;
