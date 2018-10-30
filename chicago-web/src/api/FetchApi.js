export default class FetchApi {
  defaultFetch(url, serialized_obj, deserializer, errorHandler) {
    console.log('FetchApi url=', url);
    
    return fetch(url, {
      method: "POST",
      body: serialized_obj,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      console.log('FetchApi response=', response);

      if (!response.ok) {
        throw response;
      }
      return response.arrayBuffer();
    }).then(proto => {
      console.log('FetchApi state proto=', proto);
      let response = deserializer(proto);
      console.log('FetchApi state response=', response);
  //    console.log('FetchApi state response.getTransactionError()=',response.getTransactionError());
      if (response.getTransactionError() !== undefined) {
        if (errorHandler !== null) {
          errorHandler(response.getTransactionError().getErrorMessage());
        }
      } else {
        console.log('FetchApi return response=', response);
        return response;
      }
    }).catch(rest_error => {
      console.log('FetchApi catch  rest_error=',rest_error);
      if (rest_error.status === 500) {
        // Show error
        if (errorHandler !== null) {
          errorHandler("Error 500. Server error.");
        }
        return;
      }
      rest_error.json().then(errorMessage => {
        if (errorHandler !== null) {
          errorHandler(errorMessage);
        }
      })
    })
  }

// без проверки response.getTransactionError() - она нужна?

  permissionFetch(url, serialized_obj, deserializer, errorHandler) {
    console.log('FetchApi url=', url);
    
    return fetch(url, {
      method: "POST",
      body: serialized_obj,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      console.log('FetchApi response=', response);

      if (!response.ok) {
        throw response;
      }
      return response.arrayBuffer();
    }).then(proto => {
      console.log('FetchApi state proto=', proto);
      let response = deserializer(proto);
      console.log('FetchApi state response=', response);
      return response;
    }).catch(rest_error => {
      console.log('FetchApi catch  rest_error=',rest_error);
      if (rest_error.status === 500) {
        // Show error
        if (errorHandler !== null) {
          errorHandler("Error 500. Server error.");
        }
        return;
      }
      rest_error.json().then(errorMessage => {
        if (errorHandler !== null) {
          errorHandler(errorMessage);
        }
      })
    })
  }

}
