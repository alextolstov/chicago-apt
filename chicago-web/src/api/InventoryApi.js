import FetchApi from './FetchApi'
import InventoryConvertor from "../convertors/InventoryConvertor";

const inventory_proto = require('dto/inventory_pb.js');
const inventorymessages_proto = require('dto/inventorynmessages_pb.js');

export default class OrganizationApi {
  constructor(){
    this.createOrganizationUrl = '/api/organization/create';
    this.updateOrganizationUrl = '/api/organization/update';
    this.getOrganizationUrl = '/api/organization/get';
    this.getStructureUrl = '/api/organization/getstructure';
    this.fetchApi = new FetchApi();
    this.convertor = new OrganizationConvertor();
  }

  createOrganization(organization, errorHandler) {
    let dtoOrg = new organization_proto.Organization();
    this.convertor.toDto(organization, dtoOrg);
    return this.fetchApi.restCrud(this.createOrganizationUrl, dtoOrg, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  updateOrganization(organization, errorHandler) {
    let dtoOrg = new organization_proto.Organization();
    this.convertor.toDto(organization, dtoOrg);
    return this.fetchApi.restCrud(this.updateOrganizationUrl, dtoOrg, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  getOrganization(organizationId, errorHandler) {
    let organization = new organization_proto.Organization();
    organization.setOrganizationId(organizationId);
    return this.fetchApi.restCrud(this.getOrganizationUrl, organization, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  getStructure(errorHandler) {
    return this.fetchApi.defaultFetch(this.getStructureUrl,
      "",
      organizationmessages_proto.OrganizationStructureResponse.deserializeBinary,
      errorHandler);
  }

}
