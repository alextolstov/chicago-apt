class UiAddress {
  constructor() {
    this.address_id = "";
    this.user_id = "";
    this.street_name = "";
    this.house_number = "";
    this.building_info = "";
    this.office_apt_number = '';
    this.city = "";
    this.place_name = ""; // Village
    this.county = ""; //Obl, Kraj, County
    this.state = ""; // Republic
    this.zip_code = "";
    this.country = "";
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiAddress;

