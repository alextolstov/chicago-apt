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

    return this.addressCrud(this.createAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (addressMsg) {
        return self.getUiAddress(self, addressMsg);
      });
  }

  updateAddress(uiAddress, errorHandler) {
    let protoAddress = new address_proto.Address();
    this.convertor.toDto(uiAddress, protoAddress);
    let self = this;

    return this.addressCrud(this.updateAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (addressMsg) {
        return self.getUiAddress(self, addressMsg);
      });
  }

  getAddress(addressId, errorHandler) {
    let protoAddress = new address_proto.Address();
    protoAddress.setAddressId(addressId);
    let self = this;

    return this.addressCrud(this.getAddressUrl, protoAddress, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler)
      .then(function (addressMsg) {
        return self.getUiAddress(self, addressMsg);
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

  addressCrud(url, protoData, deserializer, errorHandler){
    let serialized_object = protoData.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
