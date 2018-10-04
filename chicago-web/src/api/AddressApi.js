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
    return this.addressCrud(this.createAddressUrl, address, context);
  }

  updateAddress(address, context) {
    return this.addressCrud(this.updateAddressUrl, address, context);
  }

  getAddress(address, context) {
    return this.addressCrud(this.getAddressUrl, address, context);
  }

  addressCrud(url, address, context){
    let serialized_address = address.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_address,
      addressmessages_proto.AddressResponse.deserializeBinary,
      context);
  }
}
