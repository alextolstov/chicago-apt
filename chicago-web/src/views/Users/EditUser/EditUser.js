import React, {Component} from 'react';
import { TextMask, InputAdapter } from 'react-text-mask-hoc';
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

// React select
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
const user_proto = require('models/user_pb');

const messages = defineMessages({
  firstNamePlace: {
    id: 'users.edit.firstname',
    defaultMessage: 'First Name'
  },
  midleNamePlace: {
    id: 'users.edit.midlename',
    defaultMessage: 'Midle Name'
  },
  lastNamePlace: {
    id: 'users.edit.lastname',
    defaultMessage: 'Last Name'
  },
  emailPlace: {
    id: 'login.email',
    defaultMessage: 'Email'
  },
  nickNamePlace: {
    id: 'users.edit.nickname',
    defaultMessage: 'Nickname'
  },
  homePhonePlace: {
    id: 'users.edit.homephone',
    defaultMessage: 'Home phone'
  },
  cellPhonePlace: {
    id: 'users.edit.cellphone',
    defaultMessage: 'Cell phone'
  },
  taxPayerPlace: {
    id: 'users.edit.taxpayerid',
    defaultMessage: 'Tax payer id'
  },
  diplomaPlace: {
    id: 'users.edit.diploma',
    defaultMessage: 'Diploma'
  },
  retirementPlace: {
  id: 'users.edit.retirement',
    defaultMessage: 'Retirement'
  },
  medicalPlace: {
    id: 'users.edit.medical',
    defaultMessage: 'Medical'
  },
  employmentPlace: {
    id: 'users.edit.employment',
    defaultMessage: 'Employment'
  },
  passportPlace: {
    id: 'users.edit.passport',
    defaultMessage: 'Passport'
  }
});

const positions = [
  { value: '01', label: 'Barber'},
  { value: '02', label: 'Manager' },
  { value: '03', label: 'Supervisor' },
  { value: '04', label: 'Cleaner' },
  { value: '05', label: 'Somebody' }
];

class EditUser extends Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.saveSelectChanges = this.saveSelectChanges.bind(this);

    this.state = {
      value: ['01', '02'],
      user_id: props.match.params.id,
      user: new user_proto.User()
    };
  }

  saveSelectChanges(value) {
    this.setState({ value });
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  render() {
    return (
      <div className="animated fadeIn">
        <Row>
          <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
            <Card>
              <CardHeader>
                <button><i className="icon-note"></i></button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/></strong>
              </CardHeader>

              <CardBody>
                {/*Email*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-envelope-o"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.emailPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="email" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*First name*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.firstNamePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="firstname" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Middle name*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.midleNamePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="midlename" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Last name*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.lastNamePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="lastname" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Nickname*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.nickNamePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="nickname" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Passport*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-id-card"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.passportPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="passport" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/* DOB */}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.dob" defaultMessage="DOB"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input" name="date-input" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Home phone*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-phone"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.homePhonePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="homephone" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

                {/*Cell phone*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-phone"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.cellPhonePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="cellphone" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                </FormGroup>

              </CardBody>
            </Card>
          </Col>

          <Col sm={12} md={6}>
            <Card>
              <CardHeader>
                <button><i className="icon-note"></i></button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Attributes"/></strong>
              </CardHeader>

              <CardBody>
                {/*Employment date*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.employmentdate" defaultMessage="Employment date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-employment" name="date-input-employment" placeholder="date"/>
                      {/*Actual start date*/}
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actualstartdate" defaultMessage="Start date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-actualstart" name="date-input-actualstart" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Dismissal date*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.dismissaldate" defaultMessage="Dismissal date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-dismissal" name="date-input-dismissal" placeholder="date"/>
                      {/*Actual last date*/}
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actuallastdate" defaultMessage="Last date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-actuallast" name="date-input-actuallast" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Tax payer id*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-male"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.taxPayerPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="taxpayerid" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      <Input type="date" id="date-input-employment" name="date-input-employment" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Diploma*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="cui-book"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.diplomaPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="diploma" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      {/*Diploma date*/}
                      <Input type="date" id="date-input-diploma" name="date-input-diploma" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Retirement*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="cui-book"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.retirementPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="retirement" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      {/*Diploma date*/}
                      <Input type="date" id="date-input-retirement" name="date-input-retirement" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Medical*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="cui-book"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.medicalPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="retirement" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      {/*Diploma date*/}
                      <Input type="date" id="date-input-medical" name="date-input-medical" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Employment book*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="cui-book"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.employmentPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="employment" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      {/*Employment book date*/}
                      <Input type="date" id="date-input-employment" name="date-input-employment" placeholder="date"/>
                    </InputGroup>
                </FormGroup>

                {/*Positions*/}
                <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-id-card"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Col md="10">
                      <Select
                        name="form-field-positions"
                        value={this.state.value}
                        options={positions}
                        onChange={this.saveSelectChanges}
                        multi
                      />
                      </Col>
                    </InputGroup>
                </FormGroup>

              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default inject("appStore")(observer(EditUser));

