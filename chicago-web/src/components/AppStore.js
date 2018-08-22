import { extendObservable } from 'mobx'

class AppStore {
  constructor () {
    extendObservable(this, {
      permissions: [],
      userData: ""
    });
  }
}

export default AppStore;
