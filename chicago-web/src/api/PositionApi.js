import FetchApi from './FetchApi'
import UiPosition from "../models/UiPosition";
import PositionConvertor from "../convertors/PositionConvertor";

const position_proto = require('dto/position_pb.js');
const positionmessages_proto = require('dto/positionmessages_pb.js');
const organization_proto = require('dto/organization_pb.js');
const organizationmessages_proto = require('dto/organizationmessages_pb.js');

export default class PositionApi {
  constructor() {
    this.createPositionUrl = '/api/position/create';
    this.updatePositionUrl = '/api/position/update';
    this.deletePositionUrl = '/api/position/delete';
    this.getPositionsUrl = '/api/position/get';
    this.fetchApi = new FetchApi();
    this.convertor = new PositionConvertor();
  }

  createPosition(organizationId, description, errorHandler) {
    let position = new position_proto.Position();
    position.setOrganizationId(organizationId);
    position.setDescription(description);
    let self = this;
    return this.fetchApi.restCrud(this.createPositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPosition(self, msg);
      });
  }

  updatePosition(position, errorHandler) {
    let self = this;
    return this.fetchApi.restCrud(this.updatePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
      return self.getUiPosition(self, msg);
    });
  }

  deletePosition(organizationId, position, errorHandler) {
    return this.fetchApi.restCrud(this.deletePositionUrl, position, positionmessages_proto.PositionResponse.deserializeBinary, errorHandler);
  }

  getPositions(organizationId, errorHandler) {
    let organization = new organization_proto.OrganizationId();
    organization.setOrganizationId(organizationId);
    let self = this;
    return this.fetchApi.restCrud(this.getPositionsUrl, organization, positionmessages_proto.PositionsResponse.deserializeBinary, errorHandler)
      .then(function (msg) {
        return self.getUiPositions(self, msg);
      });
  }

  getUiPosition(self, msg) {
    let savedPos = msg.getPosition();
    let uiPosition = null;
    if (savedPos != null) {
      uiPosition = new UiPosition();
      self.convertor.fromDto(savedPos, uiPosition);
    }
    return Promise.resolve(uiPosition);
  }

  getUiPositions(self, msg) {
    if (msg.getPositions === undefined) {
      return Promise.resolve([]);
    }
    let savedPos = msg.getPositions().getPositions();
    let positionsMap = {};

    if (savedPos !== undefined && savedPos !== null) {
      savedPos.forEach((l, v) => {
        positionsMap.push({value:v, label:l});
      });
    }
    return Promise.resolve(positionsMap);
  }

}
