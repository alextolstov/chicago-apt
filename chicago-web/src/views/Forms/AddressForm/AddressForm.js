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
  Row
} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-select/dist/react-select.min.css';
import FormApi from '../../../api/FormApi';
import UserApi from '../../../api/UserApi';
import DateTimeApi from "../../../api/DateTimeApi";

const address_proto = require('models/address_pb');

const messages = defineMessages({
  streetPlace: {
    id: 'address.edit.street',
    defaultMessage: 'Street'
  },
  housePlace: {
    id: 'address.edit.house',
    defaultMessage: 'House'
  }
});

class AddressForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      address : new address_proto.Address(),
      initWritable: false,
      dateTime: new DateTimeApi(),
      formApi: new FormApi()
    };
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "office_number":
        this.state.address.setOfficeNumber(event.target.value);
        break;
      case "apartment_number":
        this.state.address.setApartmentNumber(event.target.value);
        break;
      case "house_number":
        this.state.address.setHouseNumber(event.target.value);
        break;
      case "building_info":
        this.state.address.setBuildingInfo(event.target.value);
        break;
      case "street_name":
        this.state.address.setStreetName(event.target.value);
        break;
      case "city":
        this.state.address.setCity(event.target.value);
        break;
      case "place_name":
        this.state.address.setPlaceName(event.target.value);
        break;
      case "area":
        this.state.address.setArea(event.target.value);
        break;
      case "administrative_division":
        this.state.address.setAdministrativeDivision(event.target.value);
        break;
      case "state":
        this.state.address.setState(event.target.value);
        break;
      case "zip_code":
        this.state.address.setZipCode(event.target.value);
        break;
      case "country":
        this.state.address.setCountry(event.target.value);
        break;
    }
    this.setState({[event.target.id]: event.target.value});
  }

  render() {
    return (
      <Card id='address'>
        <CardHeader>
          <button><i className="icon-cloud-upload" onClick={this.handleUserInfo}></i></button>
          <strong><FormattedMessage id="users.edit.address" defaultMessage="Address"/></strong>
          <div className="card-header-actions">
            <AppSwitch id="address_enabled" onClick={(e) => this.state.formApi.handleFormEnableDisable('address', e)}
                       className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                       label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
          </div>
        </CardHeader>
        <CardBody>
          {/*Street*/}
          <FormGroup row>
            <InputGroup>
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <i className="fa fa-envelope-o"></i>
                </InputGroupText>
              </InputGroupAddon>
              <FormattedMessage {...messages.streetPlace}>
                {
                  pholder => <Input onChange={this.handleChange} value={this.state.address.getStreet == undefined ? "" : this.state.address.getStreet()}
                                    type="text" id="street" name="street" placeholder={pholder} required/>
                }
              </FormattedMessage>
            </InputGroup>
          </FormGroup>

          {/*House*/}
          <FormGroup row>
            <InputGroup>
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <i className="fa fa-user"></i>
                </InputGroupText>
              </InputGroupAddon>
              <FormattedMessage {...messages.housePlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getHouseNumber == undefined ? "" : this.state.address.getHouseNumber()}
                                    type="text" id="house_number" name="house_number" placeholder={pholder} required/>
                }
              </FormattedMessage>
            </InputGroup>
          </FormGroup>
        </CardBody>

      </Card>
    );
  }
}

export default AddressForm;
