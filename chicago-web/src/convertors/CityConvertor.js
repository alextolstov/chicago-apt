import {UiCity, UiDistrict, UiSubwayLine, UiSubwayStation} from "../models/UiCity";
const city_proto = require('dto/city_pb');

class SearchFiltersConvertor {
  fromDto = (dtoObj) => {
    console.log(dtoObj);
    let uiObj = new UiCity();

    uiObj.city_id = dtoObj.getCityId !== undefined ? dtoObj.getCityId() : null;
    let districts = dtoObj.getDistrictsList != undefined ? dtoObj.getDistrictsList() : null;
    if (districts !== null) {
      uiObj.districts = new Array();
      for (let d of districts) {
        let district = new UiDistrict();
        district.district_id = d.getDistrictId();
        district.district_name = d.getDistrictName();
        district.district_classifier = d.getDistrictClassifier();
        uiObj.districts.push(district);
      }
    }

    let subway_lines = dtoObj.getSubwayLinesList !== undefined ? dtoObj.getSubwayLinesList() : null;
    if (subway_lines !== null) {
      uiObj.subway_lines = new Array();

      for (let sl of subway_lines) {
        let subway_line = new UiSubwayLine();
        subway_line.line_id = sl.getLineId();
        subway_line.line_name = sl.getLineName();
        subway_line.subway_stations = new Array();
        let stations = sl.getSubwayStationsList !== undefined ? sl.getSubwayStationsList() : null;

        if (stations !== null) {
          for (let st of stations) {
            let station = new UiSubwayStation();
            station.station_id = st.getStationId();
            station.station_name = st.getStationName();
            subway_line.subway_stations.push(station);
          }
          uiObj.subway_lines.push(subway_line);
        }
      }
    }
    return uiObj;
  }

  toDto = (uiObj) => {
    let dtoObj = new city_proto.City();
    dtoObj.setCityId(uiObj.city_id);

    if (uiObj.subway_lines !== null) {
      for (let sl of uiObj.subway_lines) {
        let line = new city_proto.SubwayLine();
        line.line_id = sl.line_id;
        line.line_name = sl.line_name;

        if (sl.subway_stations !== null) {
          for (let ss of uiObj.subway_stations) {
            let station = new city_proto.SubwayStation();
            station.station_id = ss.station_id;
            station.station_name = ss.station_name;
          }
        }
      }
    }

    if (uiObj.districts !== null) {
      for (let d of uiObj.districts) {
        let district = new city_proto.District();
        district.district_id = d.district_id;
        district.district_name = d.district_name;
        district.district_classifier = d.district_classifier;
        dtoObj.addSubwayStationId(district);
      }
    }

    return dtoObj;
  }
}

export default SearchFiltersConvertor;
