import UiProperty from "../models/UiProperty";
const property_proto = require('dto/SearchFilters');

class PropertyConvertor {
  fromDto = (dtoObj) => {
    let uiObj = new UiProperty();
    uiObj.property_id = dtoObj.getPropertyId !== undefined ? dtoObj.getPropertyId() : null;
    uiObj.city = dtoObj.getCity !== undefined ? dtoObj.getCity() : null;
    uiObj.street = dtoObj.getStreet !== undefined ? dtoObj.getStreet() : null;
    uiObj.house = dtoObj.getHouse !== undefined ? dtoObj.getHouse() : null;
    uiObj.floor_plan = dtoObj.getFloorPlan !== undefined ? dtoObj.getFloorPlan() : null;
    uiObj.type_id = dtoObj.getTypeId !== undefined ? dtoObj.getTypeId() : 0;
    uiObj.market_id = dtoObj.getMarketId !== undefined ? dtoObj.getMarketId() : 0;
    uiObj.apt_price = dtoObj.getAptPrice !== undefined ? dtoObj.getAptPrice() : 0;
    uiObj.apt_size = dtoObj.getAptSize !== undefined ? dtoObj.getAptSize() : 0;
    uiObj.windows_view = dtoObj.getWindowsView !== undefined ? dtoObj.getWindowsView() : 0;
    uiObj.balcony = dtoObj.getBalcony !== undefined ? dtoObj.getBalcony() : 0;
    uiObj.kitchen_size = dtoObj.getKitchenSize !== undefined ? dtoObj.getKitchenSize() : 0;
    uiObj.ceiling_height = dtoObj.getCeilingHeight !== undefined ? dtoObj.getCeilingHeight() : 0;
    uiObj.floor = dtoObj.getFloor !== undefined ? dtoObj.getFloor() : 0;
    uiObj.floors_in_house = dtoObj.getFloorsInHouse !== undefined ? dtoObj.getFloorsInHouse() : 0;
    uiObj.rooms_number = dtoObj.getRoomsNumber !== undefined ? dtoObj.getRoomsNumber() : 0;
    uiObj.floor_beams_material = dtoObj.getFloorBeamsMaterial !== undefined ? dtoObj.getFloorBeamsMaterial() : 0;
    uiObj.sewer_depreciation_percent = dtoObj.getSewerDepreciationPercent !== undefined ? dtoObj.getSewerDepreciationPercent() : 0;
    uiObj.walls_depreciation_percent = dtoObj.getWallsDepreciationPercent !== undefined ? dtoObj.getWallsDepreciationPercent() : 0;
    uiObj.complains_number = dtoObj.getComplainsNumber !== undefined ? dtoObj.getComplainsNumber() : 0;

    return uiObj;
  }

  toDto = (uiObj) => {
    let dtoObj = new property_proto.Property();
    dtoObj.setPropertId(uiObj.property_id);
    dtoObj.setCity(uiObj.city);
    dtoObj.setStreet(uiObj.street);
    dtoObj.setHouse(uiObj.house);
    dtoObj.setFloorPlan(uiObj.floor_plan);
    dtoObj.setTypeId(uiObj.type_id);
    dtoObj.setMarketId(uiObj.market_id);
    dtoObj.setAptPrice(uiObj.apt_price);
    dtoObj.setAptSize(uiObj.apt_size);
    dtoObj.setWindowsView(uiObj.windows_view);
    dtoObj.setBalcony(uiObj.balcony);
    dtoObj.setKitchenSize(uiObj.kitchen_size);
    dtoObj.setCeilingHeight(uiObj.ceiling_height);
    dtoObj.setFloor(uiObj.floor);
    dtoObj.setFloorsInHouse(uiObj.floors_in_house);
    dtoObj.setRoomsNumber(uiObj.rooms_number);
    dtoObj.getFloorBeamsMaterial(uiObj.floor_beams_material);
    dtoObj.getSewerDepreciationPercent(uiObj.sewer_depreciation_percent);
    dtoObj.getWallsDepreciationPercent(uiObj.walls_depreciation_percent);
    dtoObj.getComplainsNumber(uiObj.complains_number);
    return dtoObj;
  }
}

export default PropertyConvertor;
