import React, {Component} from 'react';
import {toast, ToastContainer} from 'react-toastify';
import {defineMessages, FormattedMessage} from 'react-intl';
import {Card, CardBody, CardHeader, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-toastify/dist/ReactToastify.css';
//import 'react-select/dist/react-select.min.css';
import FormApi from '../../../api/FormApi';
import DateTimeApi from "../../../api/DateTimeApi";
import AddressApi from "../../../api/AddressApi";
import UiAddress from "../../../models/UiAddress";

//const address_proto = require('dto/address_pb');

const messages = defineMessages({
  streetPlace: {
    id: 'address.edit.street',
    defaultMessage: 'Street'
  },
  housePlace: {
    id: 'address.edit.house',
    defaultMessage: 'House'
  },
  buildingPlace: {
    id: 'address.edit.building',
    defaultMessage: 'Building'
  },
  officeAptPlace: {
    id: 'address.edit.officeapt',
    defaultMessage: 'Office/Apt.'
  },
  cityPlace: {
    id: 'address.edit.city',
    defaultMessage: 'City'
  },
  placenamePlace: {
    id: 'address.edit.place',
    defaultMessage: 'Place/Village'
  },
  countyPlace: {
    id: 'address.edit.administrative_division',
    defaultMessage: 'County'
  },
  statePlace: {
    id: 'address.edit.state',
    defaultMessage: 'State'
  },
  zipPlace: {
    id: 'address.edit.zip',
    defaultMessage: 'Zip'
  },
  countryPlace: {
    id: 'address.edit.country',
    defaultMessage: 'Country'
  }
});

class AddressForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      address: new UiAddress(),
      dateTime: new DateTimeApi(),
      formApi: new FormApi(),
      addressApi: new AddressApi()
    };

    if (props.addressId !== "") {
      let self = this;
      this.state.addressApi.getAddress(props.addressId).then(function (uiAddress) {
        if (uiAddress != null) {
          self.setState({address: uiAddress});
        }
      });
    }
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "office_apt_number":
        this.state.address.office_apt_number = event.target.value;
        break;
      case "house_number":
        this.state.address.house_number = event.target.value;
        break;
      case "building_info":
        this.state.address.billing_info = event.target.value;
        break;
      case "street_name":
        this.state.address.street_name = event.target.value;
        break;
      case "city":
        this.state.address.city = event.target.value;
        break;
      case "place_name":
        this.state.address.place_name = event.target.value;
        break;
      case "county":
        this.state.address.county = event.target.value;
        break;
      case "state":
        this.state.address.state = event.target.value;
        break;
      case "zip_code":
        this.state.address.zip_code = event.target.value;
        break;
      case "country":
        this.state.address.country = event.target.value;
        break;
      default:
        break;  
    }
    this.setState({[event.target.id]: event.target.value});
  }

  handleSaveAddress = (event) => {
    // Even disabled button fire event
    if (event.target.parentNode.disabled === true) {
      return;
    }
    this.state.address.user_id = this.props.userId;
    let self = this;

    if (this.state.address.address_id === "") {
      this.state.addressApi.createAddress(this.state.address, this).then(function (uiAddress) {
        if (uiAddress != null) {
          self.setState({address: uiAddress});
          self.handleSuccess();
        }
      });
    } else {
      this.state.addressApi.updateAddress(this.state.address, this).then(function (addressMsg) {
          self.handleSuccess();
      });
    }
  }

  handleSuccess = () => {
    return toast.info('Address successfully updated...', {
      position: toast.POSITION.BOTTOM_RIGHT
    });
  }

  render() {
    const containerStyle = {
      zIndex: 1999
    };

    return (
      <div className="animated">
        <ToastContainer position="top-right" autoClose={1000} style={containerStyle}/>
        <Card id='address_card'>
          <CardHeader>
            <i><button onClick={this.handleSaveAddress}><i className="icon-cloud-upload" ></i>
            </button></i>
            <strong><FormattedMessage id="users.edit.address" defaultMessage="Address"/></strong>
            <div className="card-header-actions">
              <AppSwitch id="address_enabled" onClick={(e) => this.state.formApi.handleFormEnableDisable('address_card', e)}
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
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.street_name}
                                      type="text" id="street_name" name="street_name" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*House*/}
                <FormattedMessage {...messages.housePlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.house_number}
                                      type="text" id="house_number" name="house_number" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*Building*/}
                <FormattedMessage {...messages.buildingPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.building_info}
                                      type="text" id="building_info" name="building_info" placeholder={pholder}
                                      required/>
                  }
                </FormattedMessage>
                {/*Apt/Office*/}
                <FormattedMessage {...messages.officeAptPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.office_apt_number}
                                      type="text" id="office_apt_number" name="office_apt_number" placeholder={pholder}
                                      required/>
                  }
                </FormattedMessage>

              </InputGroup>
            </FormGroup>

            {/*City*/}
            <FormGroup row>
              <InputGroup>
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="fa fa-envelope-o"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <FormattedMessage {...messages.cityPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.city}
                                      type="text" id="city" name="city" placeholder={pholder} required/>
                  }
                </FormattedMessage>
              </InputGroup>
            </FormGroup>

            {/*Place or Village*/}
            <FormGroup row>
              <InputGroup>
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="fa fa-envelope-o"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <FormattedMessage {...messages.placenamePlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.place_name}
                                      type="text" id="place_name" name="place_name" placeholder={pholder} required/>
                  }
                </FormattedMessage>
              </InputGroup>
            </FormGroup>

            <FormGroup row>
              <InputGroup>
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="fa fa-envelope-o"></i>
                  </InputGroupText>
                </InputGroupAddon>
                {/*County*/}
                <FormattedMessage {...messages.countyPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.county}
                                      type="text" id="county" name="county" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*State*/}
                <FormattedMessage {...messages.statePlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.state}
                                      type="text" id="state" name="state" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*Country*/}
                <FormattedMessage {...messages.countryPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.country}
                                      type="text" id="country" name="country" placeholder={pholder} required/>
                  }
                </FormattedMessage>
              </InputGroup>
            </FormGroup>
          </CardBody>

        </Card>
      </div>
    );
  }
}

export default AddressForm;
