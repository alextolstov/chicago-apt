import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {Card, CardBody, CardHeader, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import 'react-select/dist/react-select.min.css';
import FormApi from '../../../api/FormApi';
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
  },
  buildingPlace: {
    id: 'address.edit.building',
    defaultMessage: 'Building'
  },
  apartmentPlace: {
    id: 'address.edit.apartment',
    defaultMessage: 'Apt.'
  },
  officePlace: {
    id: 'address.edit.office',
    defaultMessage: 'Office'
  },
  cityPlace: {
    id: 'address.edit.city',
    defaultMessage: 'City'
  },
  villagePlace: {
    id: 'address.edit.village',
    defaultMessage: 'Village'
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

  render() {
    return (
      <Card id='address_card'>
        <CardHeader>
          <button><i className="icon-cloud-upload" onClick={this.handleUserInfo}></i></button>
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
                                    value={this.state.address.getStreet == undefined ? "" : this.state.address.getStreet()}
                                    type="text" id="street" name="street" placeholder={pholder} required/>
                }
              </FormattedMessage>
              {/*House*/}
              <FormattedMessage {...messages.housePlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getHouseNumber == undefined ? "" : this.state.address.getHouseNumber()}
                                    type="text" id="house_number" name="house_number" placeholder={pholder} required/>
                }
              </FormattedMessage>
              {/*Building*/}
              <FormattedMessage {...messages.buildingPlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getBuildingNumber == undefined ? "" : this.state.address.getBuilding()}
                                    type="text" id="building_number" name="building_number" placeholder={pholder}
                                    required/>
                }
              </FormattedMessage>
              {/*Apt/Office*/}
              <FormattedMessage {...messages.apartmentPlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getApartmentNumber == undefined ? "" : this.state.address.getApartmentNumber()}
                                    type="text" id="apartment_number" name="apartment_number" placeholder={pholder}
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
                                    value={this.state.address.getCity == undefined ? "" : this.state.address.getCity()}
                                    type="text" id="city" name="city" placeholder={pholder} required/>
                }
              </FormattedMessage>
            </InputGroup>
          </FormGroup>

          {/*Village*/}
          <FormGroup row>
            <InputGroup>
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <i className="fa fa-envelope-o"></i>
                </InputGroupText>
              </InputGroupAddon>
              <FormattedMessage {...messages.villagePlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getVillage == undefined ? "" : this.state.address.getVillage()}
                                    type="text" id="village" name="village" placeholder={pholder} required/>
                }
              </FormattedMessage>
            </InputGroup>
          </FormGroup>

          {/*County*/}
          <FormGroup row>
            <InputGroup>
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <i className="fa fa-envelope-o"></i>
                </InputGroupText>
              </InputGroupAddon>
              <FormattedMessage {...messages.countyPlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getCounty == undefined ? "" : this.state.address.getCounty()}
                                    type="text" id="county" name="county" placeholder={pholder} required/>
                }
              </FormattedMessage>
              {/*State*/}
              <FormattedMessage {...messages.statePlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getState == undefined ? "" : this.state.address.getState()}
                                    type="text" id="state" name="state" placeholder={pholder} required/>
                }
              </FormattedMessage>
              {/*Country*/}
              <FormattedMessage {...messages.countryPlace}>
                {
                  pholder => <Input onChange={this.handleChange}
                                    value={this.state.address.getCountry == undefined ? "" : this.state.address.getCountry()}
                                    type="text" id="country" name="country" placeholder={pholder} required/>
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
