import React, {Component} from 'react';
import {toast, ToastContainer} from 'react-toastify';
import {defineMessages, FormattedMessage} from 'react-intl';
import {Card, CardBody, CardHeader, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-toastify/dist/ReactToastify.css';
import 'react-select/dist/react-select.min.css';
import FormApi from '../../../api/FormApi';
import DateTimeApi from "../../../api/DateTimeApi";
import AddressApi from "../../../api/AddressApi";

const address_proto = require('models/address_pb');

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
      address: new address_proto.Address(),
      dateTime: new DateTimeApi(),
      formApi: new FormApi(),
      address_api: new AddressApi()
    };

    if (props.addressId !== "") {
      let self = this;
      this.state.address.setAddressId(props.addressId);
      this.state.address_api.getAddress(this.state.address).then(function (addressMsg) {
        let savedAddress = addressMsg.getAddress();
        if (savedAddress != null) {
          self.setState({address: savedAddress});
        }
      });
    }
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "office_apt_number":
        this.state.address.setOfficeAptNumber(event.target.value);
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
      case "county":
        this.state.address.setCounty(event.target.value);
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

  handleSaveAddress = (event) => {
    // Even disabled button fire event
    if (event.target.parentNode.disabled == true) {
      return;
    }
    this.state.address.setUserId(this.props.userId);
    let callBack;
    let self = this;

    if (this.state.address.getAddressId() === "") {
      this.state.address_api.createAddress(this.state.address, this).then(function (addressMsg) {
        let savedAddress = addressMsg.getAddress();
        if (savedAddress != null) {
          self.setState({address: savedAddress});
          self.handleSuccess();
        }
      });
    } else {
      this.state.address_api.updateAddress(this.state.address, this).then(function (addressMsg) {
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
        <ToastContainer position="top-right" autoClose={5000} style={containerStyle}/>
        <Card id='address_card'>
          <CardHeader>
            <button onClick={this.handleSaveAddress}><i className="icon-cloud-upload" ></i></button>
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
                                      value={this.state.address.getStreetName === undefined ? "" : this.state.address.getStreetName()}
                                      type="text" id="street_name" name="street_name" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*House*/}
                <FormattedMessage {...messages.housePlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.getHouseNumber === undefined ? "" : this.state.address.getHouseNumber()}
                                      type="text" id="house_number" name="house_number" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*Building*/}
                <FormattedMessage {...messages.buildingPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.getBuildingInfo === undefined ? "" : this.state.address.getBuildingInfo()}
                                      type="text" id="building_info" name="building_info" placeholder={pholder}
                                      required/>
                  }
                </FormattedMessage>
                {/*Apt/Office*/}
                <FormattedMessage {...messages.officeAptPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.getOfficeAptNumber === undefined ? "" : this.state.address.getOfficeAptNumber()}
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
                                      value={this.state.address.getCity === undefined ? "" : this.state.address.getCity()}
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
                                      value={this.state.address.getPlaceName === undefined ? "" : this.state.address.getPlaceName()}
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
                                      value={this.state.address.getCounty === undefined ? "" : this.state.address.getCounty()}
                                      type="text" id="county" name="county" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*State*/}
                <FormattedMessage {...messages.statePlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.getState === undefined ? "" : this.state.address.getState()}
                                      type="text" id="state" name="state" placeholder={pholder} required/>
                  }
                </FormattedMessage>
                {/*Country*/}
                <FormattedMessage {...messages.countryPlace}>
                  {
                    pholder => <Input onChange={this.handleChange}
                                      value={this.state.address.getCountry === undefined ? "" : this.state.address.getCountry()}
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
