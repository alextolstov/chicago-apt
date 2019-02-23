import React, {Component} from 'react';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from "mobx-react/index";
import {Card, CardHeader, Col, Row,} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-select/dist/react-select.min.css';
import FormApi from '../../../api/FormApi';
import OrganizationForm from '../../Forms/OrganizationForm/OrganizationForm';

const jspb = require('google-protobuf');
const organization_proto = require('dto/organization_pb');

class EditOrganization extends Component {
  constructor(props) {
    super(props);
    this.state = {
      formApi: new FormApi(),
      organization: '',
    }
  };

  handleCreateOrghanization = () => {
    console.log('Save organization this.state.organization', this.state.organization);

  }

  componentDidMount() {
    this.state.organization = new organization_proto.Organization();
    console.log('componentDidMount this.state.organization=', this.state.organization);

  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "new_name":
        this.state.organization.setName(event.target.value);
        break;
    }
  }

  render() {
    return (
      <div className="animated fadeIn">
        <Row hidden={this.props.organizationId === 'new' ? false : true}>
          <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
            <Card id="new_user_card">
              <CardHeader>
                <button id="save_new_user" onClick={this.handleCreateOrghanization}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.new_user"
                                          defaultMessage="Create new user"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="new_user_enabled"
                             onClick={(e) => this.state.formApi.handleFormEnableDisable('new_user_card', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
              </CardHeader>
              <OrganizationForm handleChange={this.handleChange}/>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default inject("appStore")(observer(EditOrganization));
