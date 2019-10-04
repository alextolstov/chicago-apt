import UiSearchFilters from "../models/UiSearchFilters";
const searchfilters_proto = require('dto/searchfilters_pb');


class SearchFiltersConvertor {
  fromDto = (dtoObj) => {
    console.log(dtoObj);

    let uiObj = new UiSearchFilters();
    uiObj.user_id = dtoObj.getUserId != undefined ? dtoObj.getUserId() : null;
    uiObj.city_id = dtoObj.getCityId != undefined ? dtoObj.getCityId() : null;
    uiObj.subway_station_id = dtoObj.getSubwayStationIdList != undefined ? dtoObj.getSubwayStationIdList() : null;

    uiObj.type_id = dtoObj.getTypeId != undefined ? dtoObj.getTypeId() : 0;
    uiObj.market_id = dtoObj.getMarketId != undefined ? dtoObj.getMarketId() : 0;
    uiObj.rooms_number = dtoObj.getRoomsNumberList != undefined ? dtoObj.getRoomsNumberList() : null;
    uiObj.apt_price_from = dtoObj.aptPriceFrom != undefined ? dtoObj.aptPriceFrom() : 0;
    uiObj.apt_price_to = dtoObj.aptPriceTo != undefined ? dtoObj.aptPriceTo() : 0;
    uiObj.apt_size_from = dtoObj.aptSizeTo != undefined ? dtoObj.aptSizeTo() : 0;
    uiObj.apt_size_to = dtoObj.aptSizeFrom != undefined ? dtoObj.aptSizeFrom() : 0;

    uiObj.windows_view = dtoObj.getWindowsView != undefined ? dtoObj.getWindowsView() : null;
    uiObj.balcony = dtoObj.getBalcony != undefined ? dtoObj.getBalcony() : false;
    uiObj.kitchen_size_from = dtoObj.getKitchenSizeFrom != undefined ? dtoObj.getKitchenSizeFrom() : 0;
    uiObj.kitchen_size_to = dtoObj.getKitchenSizeTo != undefined ? dtoObj.getKitchenSizeTo() : 0;
    uiObj.ceiling_height = dtoObj.getCeilingHeight != undefined ? dtoObj.getCeilingHeight() : 0;

    uiObj.floor_from = dtoObj.getFloorFrom != undefined ? dtoObj.getFloorFrom() : 0;
    uiObj.floor_to = dtoObj.getFloorTo != undefined ? dtoObj.getFloorTo() : null;
    uiObj.floors_in_house_from = dtoObj.getFloorsInHouseFrom != undefined ? dtoObj.getFloorsInHouseFrom() : 0;
    uiObj.floors_in_house_to = dtoObj.getFloorsInHouseTo != undefined ? dtoObj.getFloorsInHouseTo() : 0;
    uiObj.not_first_floor = dtoObj.getNotFirstFloor != undefined ? dtoObj.getNotFirstFloor() : false;
    uiObj.not_last_floor = dtoObj.getNotLastFloor != undefined ? dtoObj.getNotLastFloor() : false;
    uiObj.last_floor = dtoObj.getLastFloor != undefined ? dtoObj.getLastFloor() : false;

    return uiObj;
  }

  toDto = (uiObj) => {
    let dtoObj = new searchfilters_proto.SearchFilters();
    dtoObj.setUserId(uiObj.user_id);
    dtoObj.setCityId(uiObj.city_id);

    for (let d of uiObj.district_id) {
      dtoObj.addDistrictId(d);
    }

    for (let s of uiObj.subway_station_id) {
      dtoObj.addSubwayStationId(s);
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
