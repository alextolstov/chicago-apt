import FetchApi from './FetchApi'

const organization_proto = require('models/organization_pb.js');
const organizationmessages_proto = require('models/organizationmessages_pb.js');

export default class OrganizationApi {
  constructor(){
    this.createOrganizationUrl = '/api/organization/create';
    this.updateOrganizationUrl = '/api/organization/update';
    this.getOrganizationUrl = '/api/organization/get';
    this.getStructureUrl = '/api/organization/getstructure';
    this.fetchApi = new FetchApi();
  }

  createOrganization(organization, errorHandler) {
    return this.organizationCrud(this.createOrganizationUrl, organization, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  updateOrganization(organization, errorHandler) {
    return this.organizationCrud(this.updateOrganizationUrl, organization, organizationmessages_proto.OrganizationResponse.deserializeBinary, errorHandler);
  }

  getOrganization(organization, errorHandler) {
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
