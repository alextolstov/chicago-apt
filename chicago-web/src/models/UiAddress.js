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

  getCopy() {
    let newObj = new UiAddress();
    newObj.address_id = this.address_id;
    newObj.user_id = this.user_id;
    newObj.street_name = this.street_name;
    newObj.house_number = this.house_number;
    newObj.building_info = this.building_info;
    newObj.office_apt_number = this.office_apt_number;
    newObj.city = this.city;
    newObj.place_name = this.place_name; // Village
    newObj.county = this.county; //Obl, Kraj, County
    newObj.state = this.state; // Republic
    newObj.zip_code = this.zip_code;
    newObj.country = this.country;
    return newObj;
  }
}

export default UiAddress;

