import FetchApi from './FetchApi'
import AddressConvertor from "../convertors/AddressConvertor";
import UiAddress from "../models/UiAddress";

const address_proto = require('dto/address_pb.js');
const addressmessages_proto = require('dto/addressmessages_pb.js');

export default class AddressApi {
  constructor(){
    this.createAddressUrl = '/api/address/create';
    this.updateAddressUrl = '/api/address/update';
    this.getAddressUrl = '/api/address/get';
    this.fetchApi = new FetchApi();
    this.convertor = new AddressConvertor();
  }

  createAddress(uiAddress, errorHandler) {
    let protoAddress = new address_proto.Address();
    this.convertor.toDto(uiAddress, protoAddress);
    let self = this;

    return this.fetchApi.restCrud(this.createAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiAddress(self, msg);
      });
  }

  updateAddress(uiAddress, errorHandler) {
    let protoAddress = new address_proto.Address();
    this.convertor.toDto(uiAddress, protoAddress);
    let self = this;

    return this.fetchApi.restCrud(this.updateAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiAddress(self, msg);
      });
  }

  getAddress(addressId, errorHandler) {
    let protoAddress = new address_proto.Address();
    protoAddress.setAddressId(addressId);
    let self = this;

    return this.fetchApi.restCrud(this.getAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiAddress(self, msg);
      });
  }

  getUiAddress(self, addressMsg) {
    let savedAddress = addressMsg.getAddress();
    let uiAddress = null;
    if (savedAddress != null) {
      uiAddress = new UiAddress();
      self.convertor.fromDto(savedAddress, uiAddress);
    }
    return Promise.resolve(uiAddress);
  }

}
