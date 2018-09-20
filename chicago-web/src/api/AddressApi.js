const address_proto = require('models/address_pb');
const addressmessages_proto = require('models/addressmessages_pb.js');

export default class AddressApi {
  createAddres(address, context) {
    let serialized_address = address.serializeBinary();
    return this.defaultFetch('/api/address/create',
      serialized_address,
      addressmessages_proto.AddressResponse,
      context);
  }

  defaultFetch(url, serialized_obj, response_type, context) {
    return fetch(url, {
      method: "POST",
      body: serialized_obj,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.arrayBuffer();
    }).then(proto => {
      let response = response_type.deserializeBinary(proto);
      if (response.getTransactionError() !== undefined) {
        context.handleError(response.getTransactionError().getErrorMessage());
      } else {
        return response;
      }
    }).catch(rest_error => {
      if (rest_error.status == 500) {
        // Show error
        context.handleError("Error 500. Server error.");
        return;
      }
      rest_error.json().then(errorMessage => {
        context.handleError(errorMessage);
      })
    })
  }
}
