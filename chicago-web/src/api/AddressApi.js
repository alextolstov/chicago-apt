import FetchApi from './FetchApi'

const addressmessages_proto = require('models/addressmessages_pb.js');

export default class AddressApi {
  constructor(){
    this.createAddressUrl = '/api/address/create';
    this.updateAddressUrl = '/api/address/update';
    this.getAddressUrl = '/api/address/get';
    this.fetchApi = new FetchApi();
  }

  createAddress(address, errorHandler) {
    return this.addressCrud(this.createAddressUrl, address, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler);
  }

  updateAddress(address, errorHandler) {
    return this.addressCrud(this.updateAddressUrl, address, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler);
  }

  getAddress(address, errorHandler) {
    return this.addressCrud(this.getAddressUrl, address, addressmessages_proto.AddressResponse.deserializeBinary, errorHandler);
  }

  addressCrud(url, address, deserializer, errorHandler){
    let serialized_object = address.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
