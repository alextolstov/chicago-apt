import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {CardBody, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText,} from 'reactstrap';
import ReactPhoneInput from 'react-phone-input-2'
import {country} from "../../../index";


const messages = defineMessages({
  name: {
    id: 'org.edit.name',
    defaultMessage: 'Name'
  },
  description: {
    id: 'org.edit.description',
    defaultMessage: 'Description'
  },
  website: {
    id: 'org.edit.website',
    defaultMessage: 'WebSite'
  },
  emaildomain: {
    id: 'org.edit.emaildomain',
    defaultMessage: 'Email'
  },
  phone: {
    id: 'org.edit.phone',
    defaultMessage: 'Phone'
  },
  fax: {
    id: 'org.edit.fax',
    defaultMessage: 'Fax'
  },
});

class OrganizationForm extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <CardBody>
        <FormGroup row>
          <InputGroup>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>
                <i className="fa fa-user-o"></i>
              </InputGroupText>
            </InputGroupAddon>
            <FormattedMessage {...messages.name}>
              {
                pholder => <Input value={this.props.organization.name} onChange={this.props.handleChange}
                                  type="text" id="name" name="name" placeholder={pholder} required/>
              }
            </FormattedMessage>
          </InputGroup>
        </FormGroup>
        <FormGroup row>
          <InputGroup>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>
                <i className="fa fa-id-card-o"></i>
              </InputGroupText>
            </InputGroupAddon>
            <FormattedMessage {...messages.description}>
              {
                pholder => <Input value={this.props.organization.description} onChange={this.props.handleChange}
                                  type="text" id="description" name="description" placeholder={pholder} required/>
              }
            </FormattedMessage>
          </InputGroup>
        </FormGroup>
        <FormGroup row>
          <InputGroup>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>
                <i className="fa fa-internet-explorer"></i>
              </InputGroupText>
            </InputGroupAddon>
            <FormattedMessage {...messages.website}>
              {
                pholder => <Input value={this.props.organization.web_site} onChange={this.props.handleChange}
                                  type="text" id="website" name="website" placeholder={pholder} required/>
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
            <FormattedMessage {...messages.emaildomain}>
              {
                pholder => <Input value={this.props.organization.email_domain} onChange={this.props.handleChange}
                                  type="text" id="emaildomain" name="emaildomain" placeholder={pholder} required/>
              }
            </FormattedMessage>
          </InputGroup>
        </FormGroup>
        <FormGroup row>
          <InputGroup>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>
                <FormattedMessage  {...messages.phone}/>
              </InputGroupText>
            </InputGroupAddon>
            <span><ReactPhoneInput defaultCountry={country} value={this.props.organization.phone}
                                   onChange={(value) => this.props.handleChange({target: {id: 'phone', value: value}})}
                                   inputStyle={{width: '100%'}}/></span>
          </InputGroup>
        </FormGroup>
        <FormGroup row>
          <InputGroup>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>
                <FormattedMessage  {...messages.fax}/>
              </InputGroupText>
            </InputGroupAddon>
            <span><ReactPhoneInput defaultCountry={country} value={this.props.organization.fax}
                                   onChange={(value) => this.props.handleChange({target: {id: 'fax', value: value}})}
                                   inputStyle={{width: '100%'}}/></span>
          </InputGroup>
        </FormGroup>
      </CardBody>
    );
  }
}

export default OrganizationForm;
