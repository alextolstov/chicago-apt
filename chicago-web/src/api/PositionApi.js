import FetchApi from './FetchApi'

const position_proto = require('dto/position_pb.js');
const positionmessages_proto = require('dto/positionmessages_pb.js');

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
    return this.fetchApi.restCrud(this.createPositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  updatePosition(position, errorHandler) {
    return this.fetchApi.restCrud(this.updatePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  deletePosition(organizationId, position, errorHandler) {
    return this.fetchApi.restCrud(this.deletePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  getPositions(organizationId, errorHandler) {
    let position = new position_proto.Position();
    position.setOrganizationId(organizationId);
    return this.fetchApi.restCrud(this.getPositionsUrl, position, positionmessages_proto.PositionsResponse.deserializeBinary, errorHandler);
  }

}
