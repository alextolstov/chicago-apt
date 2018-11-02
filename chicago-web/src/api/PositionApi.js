import FetchApi from './FetchApi'


const position_proto = require('models/position_pb.js');
const positionmessages_proto = require('models/positionmessages_pb.js');

export default class PositionApi {
  constructor(){
    this.createPositionUrl = '/api/position/create';
    this.updatePositionUrl = '/api/position/update';
    this.deletePositionUrl = '/api/position/delete';
    this.getPositionsUrl = '/api/position/get';
    this.fetchApi = new FetchApi();
  }

  createPosition(organizationId, description, errorHandler) {
    let position = new position_proto.Position();
    position.setOrganizationId(organizationId);
    position.setDescription(description);
    return this.positionCrud(this.createPositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  updatePosition(position, errorHandler) {
    return this.positionCrud(this.updatePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  deletePosition(organizationId, position, errorHandler) {
    return this.positionCrud(this.deletePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  getPositions(organizationId, errorHandler) {
    console.log('getPositions par=', organizationId, errorHandler);
    
    let position = new position_proto.Position();
    position.setOrganizationId(organizationId);
    return this.positionCrud(this.getPositionsUrl, position, positionmessages_proto.PositionsResponse.deserializeBinary, errorHandler);
  }

  positionCrud(url, userObject, deserializer, errorHandler) {
    let serialized_object = userObject.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
