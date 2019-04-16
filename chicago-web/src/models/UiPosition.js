class UiPosition {
  constructor() {
    this.organization_id = "";
    this.position_id = "";
    this.description = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiPosition;

