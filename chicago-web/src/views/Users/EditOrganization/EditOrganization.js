import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {inject, observer} from "mobx-react/index";
import {
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row,
} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
import UserApi from '../../../api/UserApi';
import FormApi from '../../../api/FormApi';
import PositionApi from '../../../api/PositionApi';
import PermissionApi from '../../../api/PermissionApi';
import DateTimeApi from '../../../api/DateTimeApi';
import AddressForm from '../../Forms/AddressForm/AddressForm';
import PositionForm from '../../Forms/PositionForm/PositionForm';
import PermissionForm from '../../Forms/PermissionForm/PermissionForm';
import OrganizationForm from '../../Forms/OrganizationForm/OrganizationForm';
import {ToastContainer, toast} from 'react-toastify';
const jspb=require('google-protobuf');
const organization_proto = require('models/organization_pb');





class EditOrganization extends Component {
  constructor(props) {
    super(props);
    this.state={
      formApi: new FormApi(),
      organization: '',
    } 
    this.handleCreateOrghanization=this.handleCreateOrghanization.bind(this);
  };
 
  handleCreateOrghanization() {
     console.log('Save organization this.state.organization',this.state.organization);
       
  }

  componentDidMount() {
    this.state.organization=new organization_proto.Organization();
    console.log('componentDidMount this.state.organization=', this.state.organization);
    
  }



  handleChange=(event) => {
    switch(event.target.id) {
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
