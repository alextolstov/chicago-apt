import {UiAddress} from "../models/UiAddress";

class AddressConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.address_id = dtoObj.getAddressId !== undefined ? dtoObj.getAddressId() : "";
    uiObj.user_id = dtoObj.getUserId !== undefined ? dtoObj.getUserId() : "";
    uiObj.street_name = dtoObj.getStreetName !== undefined ? dtoObj.getStreetName() : "";
    uiObj.house_number = dtoObj.getHouseNumber !== undefined ? dtoObj.getHouseNumber() : "";
    uiObj.building_info = dtoObj.getBuildingInfo !== undefined ? dtoObj.getBuildingInfo() : "";
    uiObj.office_apt_number = dtoObj.getOfficeAptNumber !== undefined ? dtoObj.getOfficeAptNumber() : "";
    uiObj.city = dtoObj.getCity !== undefined ? dtoObj.getCity() : "";
    uiObj.entity_id = dtoObj.getEntityId !== undefined ? dtoObj.getEntityId() : "";
    uiObj.place_name = dtoObj.getPlaceName !== undefined ? dtoObj.getPlaceName() : "";
    uiObj.county = dtoObj.getCounty !== undefined ? dtoObj.getCounty() : "";
    uiObj.state = dtoObj.getState !== undefined ? dtoObj.getState() : "";
    uiObj.zip_code = dtoObj.getZipCode !== undefined ? dtoObj.getZipCode() : "";
    uiObj.country = dtoObj.getCountry !== undefined ? dtoObj.getCountry() : "";
  }

  toDto = (uiObj, dtoObj) => {
    dtoObj.setAddressId(uiObj.address_id);
    dtoObj.setUserId(uiObj.user_id);
    dtoObj.setStreetName(uiObj.street_name);
    dtoObj.setHouseNumber(uiObj.house_number);
    dtoObj.setBuildingInfo(uiObj.building_info);
    dtoObj.setOfficeAptNumber(uiObj.office_apt_number);
    dtoObj.setCity(uiObj.city);
    dtoObj.setPlaceName(uiObj.place_name);
    dtoObj.setCounty(uiObj.county);
    dtoObj.setState(uiObj.state);
    dtoObj.setZipCode(uiObj.zip_code);
    dtoObj.setCountry(uiObj.country);
  }
}

export default AddressConvertor;
