import FetchApi from './FetchApi'
import SearchFiltersConvertor from "../convertors/SearchFiltersConvertor";
import UiSearchFilters from "../models/UiSearchFilters";
import UiProperty from "../models/UiProperty";

const searchfiltersmessages_proto = require('dto/searchfiltersmessages_pb.js');

export default class SearchFiltersApi {
  constructor(){
    this.searchUrl = '/api/search/get';
    this.searchCatalogUrl = '/api/searchcatalog/get';
    this.fetchApi = new FetchApi();
    this.convertor = new SearchFiltersConvertor();
  }

  search(uiSearchFilters, errorHandler) {
    let protoSearchFilters = this.convertor.toDto(uiSearchFilters);
    let self = this;

    return this.fetchApi.restCrud(this.searchUrl, protoSearchFilters, searchfiltersmessages_proto.SearchFiltersResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPropertiesMessage(self, msg);
      });
  }

  getSearchCatalog(userId, errorHandler) {
    let protoSearchFilters = this.convertor.toDto(uiSearchFilters);
    let self = this;

    return this.fetchApi.restCrud(this.searchCatalogUrl, userId, searchfiltersmessages_proto.SearchFiltersCatalogResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiSearchFiltersCatalogMessage(self, msg);
      });
  }

  getUiPropertiesMessage(self, msg) {
    let propertiesList = msg.getPropertyList();
    let uiPropertyList = new Array();

    for (let prop of propertiesList) {
      let uiProperty = self.convertor.fromDto(prop);
      uiPropertyList.push(uiProperty);
    }

    return Promise.resolve(uiPropertyList);
  }

  getUiSearchFiltersCatalogMessage(self, msg) {
    let filtersList = msg.getSearchFiltersCatalogList();
    let uiSearchFiltersList = new Array();

    for (let filter of filtersList) {
      let uiSearchFilter = self.convertor.fromDto(prop);
      uiSearchFiltersList.push(uiSearchFilter);
    }

    return Promise.resolve(uiSearchFiltersList);
  }
}
