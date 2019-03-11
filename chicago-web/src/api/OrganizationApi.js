import FetchApi from './FetchApi'
import OrganizationConvertor from "../convertors/OrganizationConvertor";

const organization_proto = require('dto/organization_pb.js');
const organizationmessages_proto = require('dto/organizationmessages_pb.js');

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
    return this.organizationCrud(this.createOrganizationUrl, dtoOrg, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  updateOrganization(organization, errorHandler) {
    let dtoOrg = new organization_proto.Organization();
    this.convertor.toDto(organization, dtoOrg);
    return this.organizationCrud(this.updateOrganizationUrl, dtoOrg, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  getOrganization(organizationId, errorHandler) {
    let organization = new organization_proto.Organization();
    organization.setOrganizationId(organizationId);
    return this.organizationCrud(this.getOrganizationUrl, organization, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  getStructure(errorHandler) {
    return this.fetchApi.defaultFetch(this.getStructureUrl,
      "",
      organizationmessages_proto.OrganizationStructureResponse.deserializeBinary,
      errorHandler);
  }

  organizationCrud(url, organization, deserializer, errorHandler) {
    let serialized_object = organization.serializeBinary();
    return this.fetchApi.defaultFetch(url,
      serialized_object,
      deserializer,
      errorHandler);
  }
}
