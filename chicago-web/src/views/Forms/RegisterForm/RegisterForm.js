import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {CardBody, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText,} from 'reactstrap';

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
    emailOrPhone: {
        id: 'login.emailOrPhone',
        defaultMessage: 'Email or Phone'
    },
});

class RegisterForm extends Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

  render() {
        return (
            <CardBody>
                {/*Email*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="fa fa-envelope-o"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.emailOrPhone}>
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
