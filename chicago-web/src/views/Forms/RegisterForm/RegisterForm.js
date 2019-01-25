import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {CardBody, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText,} from 'reactstrap';
import ReactPhoneInput from 'react-phone-input-2'
import {country} from "../../../index";

    
const messages = defineMessages({
    passwordPlace: {
        id: 'users.edit.password',
        defaultMessage: 'Password'
    },
    repeatPasswordPlace: {
        id: 'users.edit.repeat_password',
        defaultMessage: 'Repeat Password'
    },
    wholeNamePlace: {
        id: 'users.edit.wholename',
        defaultMessage: 'First/Middle/Last Name'
    },
    email: {
        id: 'login.email',
        defaultMessage: 'Email'
    },
    cellPhone: {
        id: 'login.cellPhone',
        defaultMessage: 'Cell Phone'
    },
});

class RegisterForm extends Component {

    constructor(props) {
        super(props);
        this.state={
            phone:""
        };
        this.handleChangePhone=this.handleChangePhone.bind(this);
    }
/*
    pholder => <Input onChange={this.props.handleChange}
    type="text" id="new_cellPhone" name="new_cellPhone" placeholder={pholder} required/>
*/
  handleChangePhone(value) {
    console.log('handleChangePhone ', value);
    this.props.handleChange({target:{ id:'new_cellPhone', value: value}})    
    this.setState({phone: value});
  }
  render() {
        return (
            <CardBody>
                {/*Cell Phone*/}
                <FormGroup row>
                    <InputGroup>
                        <FormattedMessage {...messages.cellPhone}>
                            {
                                pholder => <ReactPhoneInput defaultCountry={country} value={this.state.phone}
                                    onChange={this.handleChangePhone} inputStyle={{width: '100%'}} /> 
                              
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>
                {/*Email*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="fa fa-envelope-o"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.email}>
                            {
                                pholder => <Input onChange={this.props.handleChange}
                                    type="text" id="new_email" name="new_email" placeholder={pholder} required/>
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Whole name First/Last/Middle*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="fa fa-user"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.wholeNamePlace}>
                            {
                                pholder => <Input onChange={this.props.handleChange}
                                    type="text" id="whole_name" name="whole_name" placeholder={pholder} required/>
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Password*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="icon-lock"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.passwordPlace}>
                            {
                                pholder => <Input onChange={this.props.handleChange}
                                    type="password" id="password" name="password" placeholder={pholder} required/>
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/* Repeat Password*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="icon-lock"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.repeatPasswordPlace}>
                            {
                                pholder => <Input onChange={this.props.handleChange}
                                    type="password" id="repeat_password" name="repeat_password"
                                    placeholder={pholder}
                                    required/>
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>

            </CardBody>
        );
    }
}

export default RegisterForm;
