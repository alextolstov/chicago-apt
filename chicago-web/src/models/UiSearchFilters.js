export const ViewFromWindow = {
  VIEW_NOT_IMPORTANT: 0,
  STREET_VIEW: 1,
  BACKYARD_VIEW: 2
}

export const PropertyType = {
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

export const Market = {
  MARKET_NOT_IMPORTANT: 0,
  SECOND: 1,
  FIRST: 2
}

export const CeilingHeight = {
  HEIGHT_NOT_IMPORTANT: 0,
  FROM_2_5: 1,
  FROM_3_0: 2,
  FROM_3_5: 3,
  FROM_4_0: 4
}

export const FloorBeamsMaterial = {
  WOOD_WOOD: 0,
  METAL_WOOD: 1,
  METAL_CONCRETE: 2
}

class UiSearchFilters {
  constructor() {
    this.user_id = null;
    this.city_id = null;
    this.district_id = new Map();
    this.subway_station_id = new Map();

    this.type_id = PropertyType.APARTMENT;
    this.market_id = Market.MARKET_NOT_IMPORTANT;
    this.rooms_number = new Set();
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

  convertPropertyType = (type_id) => {
    switch (type_id) {
      case PropertyType.APARTMENT:
        return 'Квартира';
      case PropertyType.ROOM:
        return 'Комната';
      case PropertyType.SHARE:
        return 'Доля';
      case PropertyType.HOUSE:
        return 'Дом';
      case PropertyType.HOUSE_PART:
        return 'Часть дома';
      case PropertyType.OFFICE:
        return 'Офис';
      case PropertyType.RETAIL_SPACE:
        return 'Торговая площадь';
      case PropertyType.WAREHOUSE:
        return 'Склад';
      case PropertyType.RESTAURANT:
        return 'Общепит';
      case PropertyType.GARAGE:
        return 'Гараж';
      case PropertyType.AUTO_SERVICE:
        return 'Автосервис';
    }
  }

  convertMarket = (market) => {
    switch (market) {
      case Market.MARKET_NOT_IMPORTANT:
        return 'Любой'
      case Market.FIRST:
        return 'Новостройка'
      case Market.SECOND:
        return 'Вторичка'
    }
  }

  convertCeilingHeight = (ceiling) => {
    switch (ceiling) {
      case CeilingHeight.HEIGHT_NOT_IMPORTANT:
        return 'Любая';
      case CeilingHeight.FROM_2_5:
        return '2.5+';
      case CeilingHeight.FROM_3_0:
        return '3.0+';
      case CeilingHeight.FROM_3_5:
        return '3.5+';
      case CeilingHeight.FROM_4_0:
        return '4.0+';
    }
  }

  convertViewFromWindow = (windows_view) => {
    switch (windows_view) {
      case ViewFromWindow.VIEW_NOT_IMPORTANT:
        return "Не важно";
      case ViewFromWindow.STREET_VIEW:
        return "На улицу";
      case ViewFromWindow.BACKYARD_VIEW:
        return "Во двор";
    }
  }
}

export default UiSearchFilters;
