import React, {Component} from 'react';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from "mobx-react/index";
import {Card, CardHeader, Col, Row,} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-select/dist/react-select.min.css';
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
      fields: {},
      show: false,
      isLoading: true,
      phone: null
    },
      this.formApi = new FormApi(),
      this.organizationApi = new OrganizationApi(),
      this.organization = null,
      this.handleCreateOrghanization = this.handleCreateOrghanization.bind(this);
  };

  handleCreateOrghanization() {
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
    console.log('createOrUpdateOrganization state=', this.state);

    this.organization.setName(this.state.fields.Name);
    this.organization.setWebSite(this.state.fields.WebSite);
    this.organization.setDescription(this.state.fields.Description);
    this.organization.setEmailDomain(this.state.fields.EmailDomain);
    this.organization.setPhone(this.state.fields.Phone);
    this.organization.setFax(this.state.fields.Fax);
//
    this.organization.setType(0);
//    this.organization.setParentOrganizationId(this.props.company.getOrganizationId());
    this.organization.setParentOrganizationId("66567");

    if (this.props.organizationId !== "new") {
      this.organizationApi.updateOrganization(this.organization, (e) => {
          console.log('Update load:', e)
        }
      ).then(function (sobj) {
        console.log('createOrUpdateOrganization update organization ', sobj);
        if (self.props.loadList)
          self.props.loadList();       // refresh list users
        self.props.toggle();

      });
    } else {
//      console.log('createOrUpdateOrganization create before self.props.company', self.props.company);
      this.organization.id = '';
//      console.log('createOrUpdateOrganization create before organization', this.organization);

      this.organizationApi.createOrganization(this.organization, (e) => {
          console.log('Create load:', e)
        }
      ).then(function (sobj) {
        console.log('createOrUpdateOrganization create organizationMsg ', sobj);
        let newOrg = sobj.getOrganization();
//        self.props.company.addOrganizations(newOrg.getId, null);
        self.testSetIdOrganization(newOrg, 1)
        console.log('createOrUpdateOrganization create organizationObj', newOrg);


        if (self.props.loadList)
          self.props.loadList();       // refresh list users
        self.props.toggle();

      });

    }
  }

  setFields() {
    let orgObj = {};
    orgObj.Name = this.organization.getName();
    orgObj.WebSite = this.organization.getWebSite();
    orgObj.Description = this.organization.getDescription();
    orgObj.EmailDomain = this.organization.getEmailDomain();
    orgObj.Phone = this.organization.getPhone();
    orgObj.Fax = this.organization.getFax();
    this.setState({fields: orgObj});
    console.log('setFields=', this.state);
  }

  componentDidMount() {
    let self = this;
    console.log('componentDidMount this.state.organization=', this.state.organization);
    console.log('componentDidMount this.props.organizationId', this.props.organizationId);
    self.organization = new organization_proto.Organization();

    if (this.props.organizationId !== "new") {
      self.organization.setOrganizationId(this.props.organizationId);
      console.log('componentDidMount organization=', self.organization);
      this.organizationApi.getOrganization(self.organization, (e) => {
          console.log('Error load:', e)
        }
      ).then(function (obj) {
        console.log('componentDidMount read organization obj ', obj);
        self.organization = obj.getOrganization();
        self.setFields();
        //     self.updateOrganization();
        self.setState({isLoading: false})
      });
    } else {
      self.setState({isLoading: false})
    }
  }

  handleChange = (event) => {
    console.log('handleChange this,state=', this.state);
    let objFields = this.state.fields;
    switch (event.target.id) {
      case "name":
        objFields.Name = event.target.value;
        break;
      case "description":
        objFields.Description = event.target.value;
        break;
      case "website":
        objFields.WebSite = event.target.value;
        break;
      case "emaildomain":
        objFields.EmailDomain = event.target.value;
        break;
      case "phone":
        objFields.Phone = event.target.value;
        break;
      case "fax":
        objFields.Fax = event.target.value;
        break;
    }
    this.setState({fields: objFields})
  }


  render() {
    return (
      <div className="animated fadeIn">
        {!this.state.isLoading &&
        <Row>
          <Col sm={12} md={12} style={{flexBasis: 'auto'}}>
            <Card id="org_card">
              <CardHeader>
                <i><button id="save_new_user" onClick={this.createOrUpdateOrganization}>
                  <i className="icon-cloud-upload"></i>
                </button></i>
                <strong><FormattedMessage id="organization.edit"
                                          defaultMessage="Edit organization"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="new_user_enabled"
                             onClick={(e) => this.formApi.handleFormEnableDisable('org_card', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
              </CardHeader>
              <OrganizationForm fields={this.state.fields} handleChange={this.handleChange}
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
