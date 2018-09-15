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
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
import UserApi from '../../../api/UserApi';
import DateTimeApi from '../../../api/DateTimeApi';

const jspb = require('google-protobuf');
const user_proto = require('models/user_pb');

const messages = defineMessages({
  passwordPlace: {
    id: 'users.edit.password',
    defaultMessage: 'Password'
  },
  repeatPasswordPlace: {
    id: 'users.edit.repeat_password',
    defaultMessage: 'Repeat Password'
  },
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
  workPhonePlace: {
    id: 'users.edit.workphone',
    defaultMessage: 'Work phone'
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
  {value: '01', label: 'Barber'},
  {value: '02', label: 'Manager'},
  {value: '03', label: 'Supervisor'},
  {value: '04', label: 'Cleaner'},
  {value: '05', label: 'Somebody'}
];

class EditUser extends Component {
  constructor(props) {
    super(props);

    this.state = {
      date_time: new DateTimeApi(),
      value: ['01', '02'],
      user_id: props.match.params.id,
      user: "",
      // password_management_enabled: false,
      // personal_info_enabled: false,
      // attributes_enabled: false
    };

    if (this.state.user_id == 'new') {
      this.state.user = new user_proto.User();
    }
    else if (this.state.user_id == 'current') {
      this.state.user = jspb.Message.cloneMessage(this.props.appStore.userData);
    }
    else {
      let self = this;
      new UserApi().getUserById(this.state.user_id, this).then(function (user) {
        if (user != null) {
          self.state.user = user;
        }
      })
    }
  }

  componentDidMount() {
    if (this.state.user_id == 'current') {
      // this.setState({password_management_enabled: false});
      // this.setState({personal_info_enabled: false});
      // this.setState({attributes_enabled: false});
      document.getElementById('password_management_enabled').click();
      document.getElementById('personal_info_enabled').click();
      document.getElementById('attributes_enabled').click();
    }
  }

  handleFormEnableDisable = (owner_id, event) => {
    let el = document.getElementById(owner_id);
    let buttons = Array.prototype.slice.call(el.getElementsByTagName('button'), 0);
    let inputs = Array.prototype.slice.call(el.getElementsByTagName('input'), 0);
    let all = inputs.concat(buttons);
    for (let i = 0; i < all.length; i++) {
      // Do not block switch
      if (event.target.id !== all[i].id) {
        all[i].disabled = !all[i].disabled;
      }
    }
  }

  handleSavePersonalInfo = (event) => {
    console.log("Save personal")
  }

  handleError = (error) => {
    // TODO finish the function if neded
  }

  saveSelectChanges = (value) => {
    this.setState({value});
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "email":
        this.state.user.setEmail(event.target.value);
        break;
      case "first_name":
        this.state.user.setFirstName(event.target.value);
        break;
      case "midle_name":
        this.state.user.setMiddleName(event.target.value);
        break;
      case "last_name":
        this.state.user.setLastName(event.target.value);
        break;
      case "nick_name":
        this.state.user.setNickName(event.target.value);
        break;
      case "cell_phone":
        this.state.user.setCellPhone(event.target.value);
        break;
      case "home_phone":
        this.state.user.setHomePhone(event.target.value);
        break;
      case "work_phone":
        this.state.user.setWorkPhone(event.target.value);
        break;
      case "passport_number":
        this.state.user.setPassportNumber(event.target.value);
        break;
      case "date_of_birth":
        this.state.user.setDateOfBirth(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "employment_date":
        this.state.user.setEmploymentDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "actual_employment_date":
        this.state.user.setActualEmploymentDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "dismissal_date":
        this.state.user.setDismissalDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "actual_dismissal_date":
        this.state.user.setActualDismissalDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "tax_payer_id":
        this.state.user.setTaxPayerId(event.target.value);
        break;
      case "diploma_number":
        this.state.user.setDiplomaNumber(event.target.value);
        break;
      case "diploma_date":
        this.state.user.setDiplomaDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "retirement_id_number":
        this.state.user.setRetirementIdNumber(event.target.value);
        break;
      case "retirement_date":
        this.state.user.setRetirementDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "medical_book":
        this.state.user.setMedicalBook(event.target.value);
        break;
      case "medical_book_date":
        this.state.user.setMedicalBookDate(this.state.date_time.dateToUnixUTC(event.target.value));
        break;
      case "employment_book_number":
        this.state.user.setEmploymentBookNumber(event.target.value);
        break;
    }
    this.setState({[event.target.id]: event.target.value});
  }

  render() {

    return (
      <div className="animated fadeIn">
        <Row>
          <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
            <Card id="password">
              <CardHeader>
                <button id="save_personal_info" onClick={this.handleSavePersonalInfo}><i
                  className="icon-cloud-upload"></i></button>
                <strong><FormattedMessage id="users.edit.password_management"
                                          defaultMessage="Password management"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="password_management_enabled" onClick={(e) => this.handleFormEnableDisable('password', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
              </CardHeader>

              <CardBody>
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
                        pholder => <Input onChange={this.handleChange}
                                          type="password" id="email" name="email" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange}
                                          type="password" id="first_name" name="first_name" placeholder={pholder}
                                          required/>
                      }
                    </FormattedMessage>
                  </InputGroup>
                </FormGroup>

              </CardBody>
            </Card>

            <Card id="personal_info">
              <CardHeader>
                <button id="save_personal_info" onClick={this.handleSavePersonalInfo}><i
                  className="icon-cloud-upload"></i></button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="personal_info_enabled" onClick={(e) => this.handleFormEnableDisable('personal_info', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getEmail()}
                                          type="text" id="email" name="email" placeholder={pholder} required/>
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
                                          value={this.state.user.getFirstName == undefined ? "" : this.state.user.getFirstName()}
                                          type="text" id="first_name" name="first_name" placeholder={pholder} required/>
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
                                          value={this.state.user.getMiddleName == undefined ? "" : this.state.user.getMiddleName()}
                                          type="text" id="middle_name" name="middle_name" placeholder={pholder}
                                          required/>
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
                                          value={this.state.user.getLastName == undefined ? "" : this.state.user.getLastName()}
                                          type="text" id="last_name" name="last_name" placeholder={pholder} required/>
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
                                          value={this.state.user.getNickName == undefined ? "" : this.state.user.getNickName()}
                                          type="text" id="nick_name" name="nick_name" placeholder={pholder} required/>
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
                                          value={this.state.user.getPassportNumber == undefined ? "" : this.state.user.getPassportNumber()}
                                          type="text" id="passport_number" name="passport_number" placeholder={pholder}
                                          required/>
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
                    <Input type="date"
                           onChange={this.handleChange}
                           defaultValue={this.state.user.getDateOfBirth != undefined ? "" : new Date(this.state.user.getDateOfBirth()).toISOString().substr(0, 10)}
                           id="date_of_birth" name="date_of_birth" placeholder="date"/>
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
                                          value={this.state.user.getCellPhone == undefined ? "" : this.state.user.getCellPhone()}
                                          type="text" id="cell_phone" name="cell_phone" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
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
                                          value={this.state.user.getHomePhone == undefined ? "" : this.state.user.getHomePhone()}
                                          type="text" id="home_phone" name="home_phone" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                  </InputGroup>
                </FormGroup>

                {/*Work phone*/}
                <FormGroup row>
                  <InputGroup>
                    <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i className="fa fa-phone"></i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <FormattedMessage {...messages.workPhonePlace}>
                      {
                        pholder => <Input onChange={this.handleChange}
                                          value={this.state.user.getWorkPhone == undefined ? "" : this.state.user.getWorkPhone()}
                                          type="text" id="work_phone" name="work_phone" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                  </InputGroup>
                </FormGroup>

              </CardBody>
            </Card>
          </Col>

          <Col sm={12} md={6}>
            <Card id='attributes'>
              <CardHeader>
                <button><i className="icon-cloud-upload"></i></button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Attributes"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="attributes_enabled" onClick={(e) => this.handleFormEnableDisable('attributes', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
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
                    <Input type="date" id="employment_date" name="employment_date" placeholder="date"/>
                    {/*Actual start date*/}
                    <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i>
                          <FormattedMessage id="users.edit.actualstartdate" defaultMessage="Start date"/>
                        </i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <Input type="date" id="actual_employment_date" name="actual_employment_date" placeholder="date"/>
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
                    <Input type="date" id="dismissal_date" name="dismissal_date" placeholder="date"/>
                    {/*Actual last date*/}
                    <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i>
                          <FormattedMessage id="users.edit.actuallastdate" defaultMessage="Last date"/>
                        </i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <Input type="date" id="actual_dismissal_date" name="actual_dismissal_date" placeholder="date"/>
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
                                          value={this.state.user.getTaxPayerId == undefined ? "" : this.state.user.getTaxPayerId()}
                                          type="text" id="tax_payer_id" name="tax_payer_id" placeholder={pholder}
                                          required/>
                      }
                    </FormattedMessage>
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
                                          value={this.state.user.getDiplomaNumber == undefined ? "" : this.state.user.getDiplomaNumber()}
                                          type="text" id="diploma_number" name="diploma_number" placeholder={pholder}
                                          required/>
                      }
                    </FormattedMessage>
                    {/*Diploma date*/}
                    <Input type="date" id="diploma_date" name="diploma_date" placeholder="date"/>
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
                                          value={this.state.user.getRetirementIdNumber == undefined ? "" : this.state.user.getRetirementIdNumber()}
                                          type="text" id="retirement_id_number" name="retirement_id_number"
                                          placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    {/*Diploma date*/}
                    <Input type="date" id="retirement_date" name="retirement_date" placeholder="date"/>
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
                                          value={this.state.user.getMedicalBook == undefined ? "" : this.state.user.getMedicalBook()}
                                          type="text" id="medical_book" name="medical_book" placeholder={pholder}
                                          required/>
                      }
                    </FormattedMessage>
                    {/*Medical book date*/}
                    <Input type="date" id="medical_book_date" name="medical_book_date" placeholder="date"/>
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
                                          value={this.state.user.getMedicalBook == undefined ? "" : this.state.user.getMedicalBook()}
                                          type="text" id="employment_book_number" name="employment_book_number"
                                          placeholder={pholder}
                                          required/>
                      }
                    </FormattedMessage>
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
                        id="positions"
                        name="positions"
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

