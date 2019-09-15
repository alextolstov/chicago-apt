export default class UiProperty {
  constructor() {
    this.property_id = "";
    this.city = "";
    this.street = "";
    this.house = "";
    this.floor_plan = null;
    this.type_id = 0;
    this.market_id = 0;

    this.apt_price = 0;
    this.apt_size = 0;

    this.windows_view = 0;
    this.balcony = false;
    this.kitchen_size = 0;
    this.ceiling_height = 0;
    this.floor = 0;
    this.floors_in_house = 0;
    this.rooms_number = 0;
    // Construction
    this.floor_beams_material = 0;
    // Extra, depreciation
    this.floor_depreciation_percent = 0;
    this.sewer_depreciation_percent = 0;
    this.walls_depreciation_percent = 0;
    // Other
    this.complains_number = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}
