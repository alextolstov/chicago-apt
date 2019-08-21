class UiProperty {
  constructor() {
    this.property_id = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiProperty;

