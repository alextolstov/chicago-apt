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
    return this.addressCrud(this.createAddressUrl, address, errorHandler);
  }

  updateAddress(address, errorHandler) {
    return this.addressCrud(this.updateAddressUrl, address, errorHandler);
  }

  getAddress(address, errorHandler) {
    return this.addressCrud(this.getAddressUrl, address, errorHandler);
  }

  addressCrud(url, address, errorHandler){
    let serialized_address = address.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_address,
      addressmessages_proto.AddressResponse.deserializeBinary,
      errorHandler);
  }
}
