import FetchApi from './FetchApi'
import SearchFiltersConvertor from "../convertors/SearchFiltersConvertor";
import UiSearchFilters from "../models/UiSearchFilters";
import UiProperty from "../models/UiProperty";

const searchfilters_proto = require('dto/searchfilters_pb.js');

export default class SearchFiltersApi {
  constructor(){
    this.searchUrl = '/api/searchproperty/get';
    this.fetchApi = new FetchApi();
    this.convertor = new SearchFiltersConvertor();
  }

  search(searchFilters, errorHandler) {
    let protoSearchFilters = new searchfilters_proto.SearchFilters();
    this.convertor.toDto(searchFilters, protoSearchFilters);
    let self = this;

    return this.fetchApi.restCrud(this.searchUrl, protoSearchFilters, searchfilters_proto.SearchFiltersResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPropertyList(self, msg);
      });
  }

  getUiPropertyList(self, propertiesMsg) {
    let propertiesList = propertiesMsg.getPropertiesList();
    let uiPropertyList = new Array();

    for (let i = 0; i < propertiesList.length; i++) {
      let uiProperty = new UiProperty();
      self.convertor.fromDto(propertiesList[i], uiProperty);
      uiPropertyList.push(uiProperty);
    }

    return Promise.resolve(uiPropertyList);
  }
}
