const ViewFromWindow = {
  VIEW_NOT_IMPORTANT: 0,
  STREET_VIEW: 1,
  BACKYARD_VIEW: 2
}

const PropertyType = {
  APARTMENT: 0,
  ROOM: 1,
  SHARE: 2,
  HOUSE: 3,
  HOUSE_PART: 4,
  OFFICE: 5,
  RETAIL_SPACE: 6,
  WAREHOUSE: 7,
  RESTAURANT: 8,
  GARAGE: 9,
  AUTO_SERVICE: 11
}

const Market = {
  MARKET_NOT_IMPORTANT: 0,
  SECOND: 1,
  FIRST: 2
}

const CeilingHeight =
{
  HEIGHT_NOT_IMPORTANT: 0,
  FROM_2_5: 1,
  FROM_3_0: 2,
  FROM_3_5: 3,
  FROM_4_0: 4
}


class UiSearchFilters {
  constructor() {
    this.city_id = null;
    this.district_id = null; // Array
    this.subway_station_id = null; // Array

    this.type_id = PropertyType.APARTMENT;
    this.market_id = Market.MARKET_NOT_IMPORTANT;
    this.rooms_number = new Set(); // Array
    this.apt_price_from = 0;
    this.apt_price_to = 0;
    this.apt_size_from = 0;
    this.apt_size_to = 0;

    this.windows_view = ViewFromWindow.VIEW_NOT_IMPORTANT;
    this.balcony = false;
    this.kitchen_size_from = 0;
    this.kitchen_size_to = 0;
    this.ceiling_height = CeilingHeight.HEIGHT_NOT_IMPORTANT;

    this.floor_from = 0;
    this.floor_to = 0;
    this.floors_in_house_from = 0;
    this.floors_in_house_to = 0;
    this.not_first_floor = false;
    this.not_last_floor = false;
    this.last_floor = false;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiSearchFilters;
