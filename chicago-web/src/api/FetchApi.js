export default class FetchApi {
  defaultFetch(url, serialized_obj, deserializer, context) {
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
      let response = deserializer(proto);
      if (response.getTransactionError() !== undefined) {
        context.handleError(response.getTransactionError().getErrorMessage());
      } else {
        return response;
      }
    }).catch(rest_error => {
      if (rest_error.status === 500) {
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
