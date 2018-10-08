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

  createPosition(position, context) {
    return this.positionCrud(this.createPositionUrl, position, context);
  }

  updatePosition(position, context) {
    return this.positionCrud(this.updatePositionUrl, position, context);
  }

  deletePosition(position, context) {
    return this.positionCrud(this.deletePositionUrl, position, context);
  }

  getPosition(position, context) {
    return this.positionCrud(this.getPositionUrl, position, context);
  }

  positionCrud(url, userObject, context) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      positionmessages_proto.PositionResponse.deserializeBinary,
      context);
  }
}
