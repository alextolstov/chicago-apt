export default class FetchApi {

  restCrud = (url, dataObject, deserializer, errorHandler) => {
    let serialized_object = dataObject.serializeBinary();
    return this.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }

  defaultFetch = (url, serialized_obj, deserializer, errorHandler) => {
        return fetch(url, {
            method: 'POST',
            body: serialized_obj,
            credentials: 'include',
            // headers: {
            //     'Content-Type': 'application/x-www-form-urlencoded'
            // }
        }).then(response => {
            if (!response.ok) {
                throw response;
            }
            return response.arrayBuffer();
        }).then(proto => {
            let response = deserializer(proto);
            if (response.getTransactionError() !== undefined) {
                if (errorHandler !== null) {
                    errorHandler(response.getTransactionError().getErrorMessage());
                }
            } else {
                return response;
            }
        }).catch(rest_error => {
            if (rest_error.status === 500) {
                // Show error
                if (errorHandler !== null) {
                    errorHandler('Error 500. Server error.');
                }
                return;
            }
            rest_error.json().then(errorMessage => {
                if (errorHandler !== null) {
                    errorHandler(errorMessage);
                }
            });
        });
    }

}
