import FetchApi from './FetchApi'

const position_proto = require('models/position_pb');
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
    this.positionCrud(this.createPositionUrl, position, context);
  }

  updatePosition(position, context) {
    this.positionCrud(this.updatePositionUrl, position, context);
  }

  deletePosition(position, context) {
    this.positionCrud(this.deletePositionUrl, position, context);
  }

  getPosition(position, context) {
    this.positionCrud(this.getPositionUrl, position, context);
  }

  positionCrud(url, position, context) {
    let serialized_position = position.serializeBinary();
    return this.fetchApi.defaultFetch(this.getPositionUrl,
      serialized_position,
      positionmessages_proto.PositionResponse.deserializeBinary,
      context);
  }
}
