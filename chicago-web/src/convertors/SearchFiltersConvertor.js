import UiSearchFilters, { CeilingHeight, Market, PropertyType, ViewFromWindow } from '../models/UiSearchFilters'
const searchfilters_proto = require('dto/searchfilters_pb');
const city_proto = require('dto/city_pb');


class SearchFiltersConvertor {
  fromDto = (dtoObj) => {
    console.log(dtoObj);

    let model = new UiSearchFilters();
    let tmpObj = dtoObj.toObject();

    model.user_id = tmpObj.userId;
    model.city_id = tmpObj.cityId;

    model.type_id = tmpObj.typeId;
    model.market_id = tmpObj.marketId;
    model.apt_price_from = tmpObj.aptPriceFrom;
    model.apt_price_to = tmpObj.aptPriceTo;
    model.apt_size_from = tmpObj.aptSizeFrom;
    model.apt_size_to = tmpObj.aptSizeTo;

    model.windows_view = tmpObj.windowsView;
    model.balcony = tmpObj.balcony;
    model.kitchen_size_from = tmpObj.kitchenSizeFrom;
    model.kitchen_size_to = tmpObj.kitchenSizeTo;
    model.ceiling_height = tmpObj.ceilingHeight;

    model.floor_from = tmpObj.floorFrom;
    model.floor_to = tmpObj.floorTo;
    model.floors_in_house_from = tmpObj.floorsInHouseFrom;
    model.floors_in_house_to = tmpObj.floorsInHouseTo;
    model.not_first_floor = tmpObj.notFirstFloor;
    model.not_last_floor = tmpObj.notLastFloor;
    model.last_floor = tmpObj.lastFloor;

    for(let d of tmpObj.districtsList) {
      model.district_id.set(d.districtId, d.districtName);
    }

    for (let s of tmpObj.subwayStationsList) {
      model.subway_station_id.set(s.getStationId(), s.getStationName());
    }

    for (let s of tmpObj.roomsNumberList) {
      model.rooms_number.add(s);
    }

    return model;
  }

  toDto = (uiObj) => {
    let dtoObj = new searchfilters_proto.SearchFilters();
    dtoObj.setUserId(uiObj.user_id);
    dtoObj.setCityId(uiObj.city_id);

    for (let [key, value] of uiObj.district_id) {
      let district = new city_proto.District();
      district.setDistrictId(key)
      district.setDistrictName(value);
      dtoObj.addDistricts(district);
    }

    for (let [key, value]  of uiObj.subway_station_id) {
      let subwayStation = new city_proto.SubwayStation();
      subwayStation.setSubwayStationId(key);
      subwayStation.setSubwayStationName(value);
      dtoObj.addSubwayStations(subwayStation);
    }

    dtoObj.setTypeId(uiObj.type_id);
    dtoObj.setMarketId(uiObj.market_id);

    for (let r of uiObj.rooms_number) {
      dtoObj.addRoomsNumber(r);
    }

    dtoObj.setAptPriceFrom(uiObj.apt_price_from);
    dtoObj.setAptPriceTo(uiObj.apt_price_to);
    dtoObj.setAptSizeFrom(uiObj.apt_size_from);
    dtoObj.setAptSizeTo(uiObj.apt_size_to);
    dtoObj.setWindowsView(uiObj.windows_view);
    dtoObj.setBalcony(uiObj.balcony);
    dtoObj.setKitchenSizeFrom(uiObj.kitchen_size_from);
    dtoObj.setKitchenSizeTo(uiObj.kitchen_size_to);
    dtoObj.setCeilingHeight(uiObj.ceiling_height);
    dtoObj.setFloorFrom(uiObj.floor_from);
    dtoObj.setFloorTo(uiObj.floor_to);
    dtoObj.setFloorsInHouseFrom(uiObj.floors_in_house_from);
    dtoObj.setFloorsInHouseTo(uiObj.floors_in_house_to);
    dtoObj.setNotFirstFloor(uiObj.not_first_floor);
    dtoObj.setNotLastFloor(uiObj.not_last_floor);
    dtoObj.setLastFloor(uiObj.last_floor);
    return dtoObj;
  }
}

export default SearchFiltersConvertor;
