class UiPermission {
  constructor() {
    this.permission_id = "";
    this.permission_name = "";
    this.permission_description = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiRole {
  constructor() {
    this.role_id = "";
    this.role_name = "";
    this.role_description = "";
    this.permissions = [];
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export {UiPermission, UiRole};

