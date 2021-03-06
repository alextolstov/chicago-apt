import FetchApi from './FetchApi'
import SearchFiltersConvertor from "../convertors/SearchFiltersConvertor";
import PropertyConvertor from "../convertors/PropertyConvertor";
import UiSearchFilters from "../models/UiSearchFilters";
import UiProperty from "../models/UiProperty";

const user_proto = require('dto/user_pb')
const searchfiltersmessages_proto = require('dto/searchfiltersmessages_pb.js');

export default class SearchFiltersApi {
  constructor(){
    this.searchUrl = '/api/search/get';
    this.searchCatalogUrl = '/api/searchfilters/get';
    this.fetchApi = new FetchApi();
    this.searchFilterConvertor = new SearchFiltersConvertor();
    this.propertyConvertor = new PropertyConvertor();
  }

  search(uiSearchFilters, errorHandler) {
    let protoSearchFilters = this.searchFilterConvertor.toDto(uiSearchFilters);
    let self = this;

    return this.fetchApi.restCrud(this.searchUrl, protoSearchFilters, searchfiltersmessages_proto.SearchFiltersResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPropertiesMessage(self, msg);
      });
  }

  getSearchCatalog(userId, errorHandler) {
    let userDto = new user_proto.UserId();
    userDto.setUserId(userId);
    let self = this;

    return this.fetchApi.restCrud(this.searchCatalogUrl, userDto, searchfiltersmessages_proto.SearchFiltersCatalogResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiSearchFiltersCatalogMessage(self, msg);
      });
  }

  getUiPropertiesMessage(self, msg) {
    let propertiesList = msg.getPropertyList();
    let uiPropertyList = new Array();

    for (let prop of propertiesList) {
      let uiProperty = self.propertyConvertor.fromDto(prop);
      uiPropertyList.push(uiProperty);
    }

    return Promise.resolve(uiPropertyList);
  }

  getUiSearchFiltersCatalogMessage(self, msg) {
    let filtersList = msg.getSearchFiltersList();
    let uiSearchFiltersList = new Array();

    for (let filter of filtersList) {
      let uiSearchFilter = self.searchFilterConvertor.fromDto(filter);
      uiSearchFiltersList.push(uiSearchFilter);
    }

    return Promise.resolve(uiSearchFiltersList);
  }
}
