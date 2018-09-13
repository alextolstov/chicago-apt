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
// React select
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
import UserApi from '../../../api/UserApi';

const jspb = require('google-protobuf');
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
    this.handleChange = this.handleChange.bind(this);
    this.saveSelectChanges = this.saveSelectChanges.bind(this);

    this.state = {
      value: ['01', '02'],
      user_id: props.match.params.id,
      user: ""
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

  handleError(error) {
    // TODO finish the function if neded
  }

  saveSelectChanges(value) {
    this.setState({value});
  }

  handleChange = event => {
    switch (event.target.id) {
      case "email":
        this.state.user.setEmail(event.target.value);
        break;
      case "firstname":
        this.state.user.setFirstName(event.target.value);
        break;
      case "midlename":
        this.state.user.setMiddleName(event.target.value);
        break;
      case "lastname":
        this.state.user.setLastName(event.target.value);
        break;
      case "dob-input":
        let unixDate = new Date(event.target.value).getTime();
        this.state.user.setDateOfBirth(unixDate);
        break;
      case "passport":
        this.state.user.setPassportNumber(event.target.value);
        break;
    }
    this.setState({[event.target.id]: event.target.value});
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getFirstName()}
                                          type="text" id="firstname" name="firstname" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getMiddleName == undefined ? "" : this.state.user.getMiddleName()}
                                          type="text" id="midlename" name="midlename" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getLastName == undefined ? "" : this.state.user.getLastName()}
                                          type="text" id="lastname" name="lastname" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getNickName == undefined ? "" : this.state.user.getNickName()}
                                          type="text" id="nickname" name="nickname" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getPassportNumber == undefined ? "" : this.state.user.getPassportNumber()}
                                          type="text" id="passport" name="passport" placeholder={pholder} required/>
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
                    <Input type="date" onChange={this.handleChange} value={()=>{if (this.state.user.getDateOfBirth != undefined) new Date(this.state.user.getDateOfBirth())}}
                           id="dob-input" name="dob-input" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getHomePhone == undefined ? "" : this.state.user.getHomePhone()}
                                          type="text" id="homephone" name="homephone" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getCellPhone == undefined ? "" : this.state.user.getCellPhone()}
                                          type="text" id="cellphone" name="cellphone" placeholder={pholder} required/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getWorkPhone == undefined ? "" : this.state.user.getWorkPhone()}
                                          type="text" id="workphone" name="workphone" placeholder={pholder} required/>
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
                    <Input type="date" id="employment-date" name="employment-date" placeholder="date"/>
                    {/*Actual start date*/}
                    <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i>
                          <FormattedMessage id="users.edit.actualstartdate" defaultMessage="Start date"/>
                        </i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <Input type="date" id="actual-start-date" name="actual-start-date" placeholder="date"/>
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
                    <Input type="date" id="dismissal-date" name="dismissal-date" placeholder="date"/>
                    {/*Actual last date*/}
                    <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i>
                          <FormattedMessage id="users.edit.actuallastdate" defaultMessage="Last date"/>
                        </i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <Input type="date" id="actual-last-date" name="actual-last-date" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getTaxPayerId == undefined ? "" : this.state.user.getTaxPayerId()}
                                          type="text" id="taxpayer-id" name="taxpayer-id" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    <Input type="date" id="employment-date" name="employment-date" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getDiplomaNumber == undefined ? "" : this.state.user.getDiplomaNumber()}
                                          type="text" id="diploma-number" name="diploma-number" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    {/*Diploma date*/}
                    <Input type="date" id="diploma-date" name="diploma-date" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getRetirementIdNumber == undefined ? "" : this.state.user.getRetirementIdNumber()}
                                          type="text" id="retirement-id-number" name="retirement-id-number" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    {/*Diploma date*/}
                    <Input type="date" id="retirement-date" name="retirement-date" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getMedicalBook == undefined ? "" : this.state.user.getMedicalBook()}
                                          type="text" id="medical-book" name="medical-book" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    {/*Medical book date*/}
                    <Input type="date" id="medical-book-date" name="medical-book-date" placeholder="date"/>
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
                        pholder => <Input onChange={this.handleChange} value={this.state.user.getMedicalBook == undefined ? "" : this.state.user.getMedicalBook()}
                                          type="text" id="employment-book" name="employment-book" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                    {/*Employment book date*/}
                    <Input type="date" id="employment-records-date" name="employment-records-date" placeholder="date"/>
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

