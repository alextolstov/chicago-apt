import FetchApi from './FetchApi'
const address_proto = require('models/address_pb');
const addressmessages_proto = require('models/addressmessages_pb.js');

export default class AddressApi {
  constructor(){
    this.createAddressUrl = '/api/address/create';
    this.updateAddressUrl = '/api/address/update';
    this.getAddressUrl = '/api/address/get';
    this.fetchApi = new FetchApi();
  }

  createAddress(address, context) {
    let serialized_address = address.serializeBinary();
    return this.fetchApi.defaultFetch(this.createAddressUrl,
      serialized_address,
      addressmessages_proto.AddressResponse.deserializeBinary,
      context);
  }

  updateAddress(address, context) {
    let serialized_address = address.serializeBinary();
    return this.fetchApi.defaultFetch(this.updateAddressUrl,
      serialized_address,
      addressmessages_proto.AddressResponse.deserializeBinary,
      context);
  }

  getAddress(address, context) {
    let serialized_address = address.serializeBinary();
    return this.fetchApi.defaultFetch(this.getAddressUrl,
      serialized_address,
      addressmessages_proto.AddressResponse.deserializeBinary,
      context);
  }
}
