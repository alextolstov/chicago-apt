import FetchApi from './FetchApi'

const positionmessages_proto = require('models/positionmessages_pb.js');

export default class PositionApi {
  constructor(){
    this.createPositionUrl = '/api/position/create';
    this.updatePositionUrl = '/api/position/update';
    this.deletePositionUrl = '/api/position/delete';
    this.getPositionUrl = '/api/position/get';
    this.fetchApi = new FetchApi();
  }

  createPosition(position, errorHandler) {
    return this.positionCrud(this.createPositionUrl, position, errorHandler);
  }

  updatePosition(position, errorHandler) {
    return this.positionCrud(this.updatePositionUrl, position, errorHandler);
  }

  deletePosition(position, errorHandler) {
    return this.positionCrud(this.deletePositionUrl, position, errorHandler);
  }

  getPosition(position, errorHandler) {
    return this.positionCrud(this.getPositionUrl, position, errorHandler);
  }

  positionCrud(url, userObject, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      positionmessages_proto.PositionResponse.deserializeBinary,
      errorHandler);
  }
}
