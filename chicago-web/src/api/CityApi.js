import FetchApi from './FetchApi'
import CityConvertor from "../convertors/CityConvertor";
import {UiCity} from "../models/UiCity";

const citymessages_proto = require('dto/citymessages_pb.js');

export default class CityApi {
  constructor(){
    this.cityUrl = '/api/city/get';
    this.fetchApi = new FetchApi();
    this.convertor = new CityConvertor();
  }

  getCity(cityId, errorHandler) {
    let uiCity = new UiCity();
    uiCity.city_id = cityId;
    let protoCity = this.convertor.toDto(uiCity);
    let self = this;

    return this.fetchApi.restCrud(this.cityUrl, protoCity, citymessages_proto.CityResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiMessage(self, msg);
      });
  }

  getUiMessage(self, msg) {
    let cityDto = msg.getCity();
    let uiCity = self.convertor.fromDto(cityDto);
    return Promise.resolve(uiCity);
  }
}
