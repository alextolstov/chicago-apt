import React, {Component} from 'react';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from "mobx-react/index";
import {Card, CardHeader, Col, Row,} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
import UiOrganization from "../../../models/UiOrganization";
import OrganizationConvertor from "../../../convertors/OrganizationConvertor";
import FormApi from '../../../api/FormApi';
import OrganizationApi from '../../../api/OrganizationApi';
import OrganizationForm from '../../Forms/OrganizationForm/OrganizationForm';
import Spinner from "../../Spinner/Spinner";

const jspb = require('google-protobuf');
const organization_proto = require('dto/organization_pb');

class EditOrganization extends Component {
  constructor(props) {
    super(props);
    this.state = {
      organization: new UiOrganization(),
      show: false,
      isLoading: true,
    },

    this.formApi = new FormApi(),
    this.organizationApi = new OrganizationApi(),
    this.convertor = new OrganizationConvertor()
  };

  componentDidMount() {
    let self = this;
    if (!this.props.isNew) {
        this.organizationApi.getOrganization(self.props.organization.organization_id, (e) => {
            console.log('Error load:', e)
          }
        ).then(function (obj) {
          let dto = obj.getOrganization();
          self.convertor.fromDto(dto, self.state.organization);
          self.setState({isLoading: false})
        });
    } else {
      // In case new organization we pass parent organization, but need only organization id as parent
      this.state.organization.parent_organization_id = self.props.organization.organization_id;
      self.setState({isLoading: false})
    }
  }

  handleCreateOrghanization = () => {
    console.log('Save organization this.organization', this.organization);
  }

  testSetIdOrganization(oOrg, type) {
    oOrg.setType(2);

    this.organizationApi.updateOrganization(oOrg, (e) => {
        console.log('Update load:', e)
      }
    ).then(function (sobj) {
      console.log('testSetIdOrganization update organization ', sobj);
    });
  }

  createOrUpdateOrganization = () => {
    let self = this;
    if (!this.props.isNew) {
      this.organizationApi.updateOrganization(this.state.organization, (e) => {
          console.log('Update load:', e)
        }
      ).then(function (sobj) {
        console.log('createOrUpdateOrganization update organization ', sobj);
      });
    } else {
      this.organizationApi.createOrganization(this.state.organization, (e) => {
          console.log('Create load:', e)
        }
      ).then(function (sobj) {
        console.log('createOrUpdateOrganization create organizationMsg ', sobj);
        // let newOrg = sobj.getOrganization();
        // self.testSetIdOrganization(newOrg, 1)
        // console.log('createOrUpdateOrganization create organizationObj', newOrg);
        //
        // if (self.props.loadList)
        //   self.props.loadList();       // refresh list users
        // self.props.toggle();
      });
    }
  }

  handleChange = (event) => {
    let tmpOrg = this.state.organization;
    switch (event.target.id) {
      case "name":
        tmpOrg.name = event.target.value;
        break;
      case "description":
        tmpOrg.description = event.target.value;
        break;
      case "website":
        tmpOrg.web_site = event.target.value;
        break;
      case "emaildomain":
        tmpOrg.email_domain = event.target.value;
        break;
      case "phone":
        tmpOrg.phone = event.target.value;
        break;
      case "fax":
        tmpOrg.fax = event.target.value;
        break;
    }
    this.setState({organization: tmpOrg})
  }

  render() {
    return (
      <div className="animated fadeIn">
        {!this.state.isLoading &&
        <Row>
          <Col sm={12} md={12} style={{flexBasis: 'auto'}}>
            <Card id="org_card">
              <CardHeader>
                <i><button id="save_org" onClick={this.createOrUpdateOrganization}>
                  <i className="icon-cloud-upload"></i>
                </button></i>
                <strong><FormattedMessage id="org.edit"
                                          defaultMessage="Edit organization"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="new_user_enabled"
                             onClick={(e) => this.formApi.handleFormEnableDisable('org_card', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
              </CardHeader>
              <OrganizationForm organization={this.state.organization} handleChange={this.handleChange}
                                handleChangePhone={this.handleChangePhone} handleChangeFax={this.handleChangeFax}/>
            </Card>
          </Col>
        </Row>
        }
        {this.state.isLoading &&
        <Spinner/>
        }
      </div>
    );
  }
}

export default inject("appStore")(observer(EditOrganization));
