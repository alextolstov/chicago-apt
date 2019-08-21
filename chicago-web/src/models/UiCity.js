class UiDistrict {
  constructor() {
    this.district_id = null;
    this.district_name = null;
    this.district_classifier = null;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiSubwayLine {
  constructor() {
    this.line_id = null;
    this.line_name = null;
    this.subway_stations = null;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiSubwayStation {
  constructor() {
    this.station_id = null;
    this.station_name = null;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

class UiCity {
  constructor() {
    this.city_id = null;
    this.districts = null; // Array
    this.subway_lines = null; // Array
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export {UiDistrict, UiSubwayLine, UiSubwayStation, UiCity};
