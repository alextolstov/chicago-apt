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
  Row,
} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
import UserApi from '../../../api/UserApi';
import FormApi from '../../../api/FormApi';
import PositionApi from '../../../api/PositionApi';
import PermissionApi from '../../../api/PermissionApi';
import DateTimeApi from '../../../api/DateTimeApi';
import AddressForm from '../../Forms/AddressForm/AddressForm';
import PositionForm from '../../Forms/PositionForm/PositionForm';
import PermissionForm from '../../Forms/PermissionForm/PermissionForm';
import RegisterForm from '../../Forms/RegisterForm/RegisterForm';
import {toast} from 'react-toastify';
import ReactPhoneInput from 'react-phone-input-2'
import {country} from "../../../index";
import Spinner from '../../Spinner/Spinner';
import UiUser from '../../../models/UiUser'
const jspb = require('google-protobuf');
const user_proto = require('dto/user_pb');

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
  },
  role: {
    id: 'users.edit.role',
    defaultMessage: 'Role'
  }

});

class EditUser extends Component {
  constructor(props) {
    super(props);

    this.state = {
      userPositions: [],
      userPermissions: [],
      userRoles: [],

      userId: (props.match && props.match.params) ? props.match.params.id : props.userId,
      user: "",
      loginUser: null,

      readyPosition: false,
      readyPermission: false,
      personal_info_enabled: false,
      attributes_enabled: false,
      permission_enabled: false,

      need_show: false,
      mobilePhone: '',
      homePhone: '',
      workPhone: '',
      readMode: true,    // исходное состояние панелей
      isLoading: true,
    };

    this.dateTimeApi = new DateTimeApi();
    this.userApi = new UserApi();
    this.positionApi = new PositionApi();
    this.permissionApi = new PermissionApi();
    this.formApi = new FormApi();

    this.dateOfBirth = '';
    this.employmentDate = '';
    this.actualEmploymentDate = '';
    this.dismissalDate = '';
    this.diplomaDate = '';
    this.retirementDate = '';
    this.medicalBookDate = '';
    this.actualDismissalDate = '';
    this.state.loginUser = this.props.appStore.userData.getCopy();

    if (this.state.userId === 'new') {
      this.state.readMode = true;    // исходное состояние панелей
      this.state.user = new UiUser();
    }
    else if (this.state.userId === 'current') {
      this.state.readMode = true;    // исходное состояние панелей
      this.state.user = this.props.appStore.userData.getCopy();
      this.state.userPositions = this.state.user.positions;
    }
  }

  handleSaveUserInfo = (event) => {
    let self = this;
    if (this.state.userId === 'new') {
      this.userApi.createUser(this.state.user, this.handleError);
      toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success New..."/>, {
        position: toast.POSITION.BOTTOM_RIGHT,
        autoClose: 1000
      });
    } else {
      // Working with existing profile
      this.state.user.positions = new Map();
      this.state.userPositions.forEach((v) => {
        this.state.user.positions.set(v, '');
      });

      this.dateBeforeSave();
      this.userApi.saveUser(this.state.user, this.handleError).then(function () {
        if (self.state.userId === 'current') {
          self.props.appStore.userData = self.state.user;
        }
        console.log('Save user data');
        toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success..."/>, {
          position: toast.POSITION.BOTTOM_RIGHT,
          autoClose: 1000
        });
        if (self.props.loadList)
          self.props.loadList();       // refresh list users
      });
    }
  }

  setUncheckedState = (mode) => {
    if (document.getElementById('email_management_enabled')) {
      if (document.getElementById('email_management_enabled').checked === mode) {
        document.getElementById('email_management_enabled').click();
      }
      if (document.getElementById('permission_management_enabled').checked === mode) {
        document.getElementById('permission_management_enabled').click();
      }
      if (document.getElementById('password_management_enabled').checked === mode) {
        document.getElementById('password_management_enabled').click();
      }
      if (document.getElementById('personal_info_enabled').checked === mode) {
        document.getElementById('personal_info_enabled').click();
      }
      if (document.getElementById('attributes_enabled').checked === mode) {
        document.getElementById('attributes_enabled').click();
      }
      if (document.getElementById('address_enabled').checked === mode) {
        document.getElementById('address_enabled').click();
      }
    }
  }

  readyPosition = (value) => {
    this.setState({readyPosition: value});
  }

  readyPermission = () => {
    this.setState({readyPermission: true});
  }

  setPosition = (user) => {
    const posMap = user.positions;
    let userPos = [];
    for (let i = 0; i < posMap.length; i++)
      userPos.push(posMap[i][0]);
    this.state.userPositions = userPos;
  }

  componentDidMount() {
    // get User
    this.state.loginUser = this.props.appStore.userData.getCopy();
    if (this.state.userId === 'new') {
      this.state.user = new UiUser();
      this.state.readMode = true;
      this.setState({isLoading: false});
    }
    else if (this.state.userId === 'current') {
      this.state.user = this.props.appStore.userData.getCopy();
      const userId = this.state.user.user_id;
      let self = this;
      self.state.readyPermission = false;
      this.userApi.getUserById(userId, null).then(function (uiUser) {
        if (uiUser != null) {
          self.state.user = uiUser;
          self.dateAfterLoad();
          self.state.phone = self.state.user.cell_phone;
          self.setPosition(self.state.user);
//          self.permissionApi.setUserPermissions(self.props.appStore, self.state.user.user_id, self.readyPermission);
          self.setState({need_show: true, isLoading: false});
          self.state.readMode = true;
          self.setUncheckedState(self.state.readMode);
        }
      }).catch(function (error) {
        console.log('Load user error:', error);
        toast.error(error, {
          position: toast.POSITION.TOP_LEFT
        });
      })
    }
    else {
      let self = this;
      self.state.readyPermission = false;
      this.userApi.getUserById(this.state.userId, null).then(function (uiUser) {
        if (uiUser != null) {
          self.state.user = uiUser;
          self.dateAfterLoad();
          self.state.phone = self.state.user.cell_phone;
          self.setPosition(self.state.user);
          //self.permissionApi.setUserPermissions(self.props.appStore, self.state.user.user_id, self.readyPermission);
          self.setState({need_show: true, isLoading: false});
          self.state.readMode = true;
          self.setUncheckedState(self.state.readMode);
        }
      }).catch(function (error) {
        console.log('Load user error:', error);
        toast.error(error, {
          position: toast.POSITION.TOP_LEFT
        });
      })
    }
  }

  dateAfterLoad() {
    this.dateOfBirth = new Date(this.state.user.date_of_birth).toISOString().substr(0, 10);
    this.employmentDate = new Date(this.state.user.employment_date).toISOString().substr(0, 10);
    this.actualEmploymentDate = new Date(this.state.user.actual_employment_date).toISOString().substr(0, 10);
    this.dismissalDate = new Date(this.state.user.dismissal_date).toISOString().substr(0, 10);
    this.diplomaDate = new Date(this.state.user.diploma_date).toISOString().substr(0, 10);
    this.retirementDate = new Date(this.state.user.retirement_date).toISOString().substr(0, 10);
    this.medicalBookDate = new Date(this.state.user.medical_book_date).toISOString().substr(0, 10);
    this.actualDismissalDate = new Date(this.state.user.actual_dismissal_date).toISOString().substr(0, 10);
    this.state.mobilePhone = this.state.user.cell_phone;
    this.state.homePhone = this.state.user.home_phone;
    this.state.workPhone = this.state.user.work_phone;
  }

  dateBeforeSave() {
    this.state.user.setDateOfBirth(this.dateTimeApi.dateToUnixUTC(this.dateOfBirth));
    this.state.user.setEmploymentDate(this.dateTimeApi.dateToUnixUTC(this.employmentDate));
    this.state.user.setActualEmploymentDate(this.dateTimeApi.dateToUnixUTC(this.actualEmploymentDate));
    this.state.user.setDismissalDate(this.dateTimeApi.dateToUnixUTC(this.dismissalDate));
    this.state.user.setDiplomaDate(this.dateTimeApi.dateToUnixUTC(this.diplomaDate));
    this.state.user.setRetirementDate(this.dateTimeApi.dateToUnixUTC(this.retirementDate));
    this.state.user.setMedicalBookDate(this.dateTimeApi.dateToUnixUTC(this.medicalBookDate));
    this.state.user.setActualDismissalDate(this.dateTimeApi.dateToUnixUTC(this.actualDismissalDate));
  }

  componentDidUpdate(prevProps, prevState, prevContext) {
    if (this.state.need_show === true) {
      this.state.need_show = false;
      this.state.readyPermission = false;
      let self = this;
      this.userApi.getUserById(this.state.userId, null).then(function (uiUser) {
        if (uiUser != null) {
          self.state.user = uiUser;
          self.dateAfterLoad();
          self.state.phone = self.state.user.cell_phone;
          self.setPosition(self.state.user);
          self.permissionApi.setUserPermissions(self.props.appStore, self.state.user.user_id, self.readyPermission);
          self.setState({need_show: false, isLoading: false});
          self.state.readMode = true;
          self.setUncheckedState(self.state.readMode);
        }
      });
    }
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    if (nextProps && nextProps.match && nextProps.match.params) {
      if (nextProps.match.params.id !== prevState.userId && nextProps.match.params.id !== 'current') {
        return {userId: nextProps.match.params.id, isLoading: true};
      }
      if (nextProps.match.params.id !== 'current' && nextProps.match.params.id !== prevState.userId) {
        return {userId: nextProps.match.params.id, need_show: true, readMode: true, isLoading: true};
      }
      if (nextProps.match.params.id === 'current') {
        return {userId: nextProps.match.params.id, isLoading: false};
      }
    }
    return null;
  }

  handleFormEnableDisable = (name) => {
    const {personal_info_enabled, attributes_enabled, permission_enabled} = this.state;
    if (name === 'personal_info_enabled') {
      this.setState({personal_info_enabled: !personal_info_enabled});
    }
    else if (name === 'attributes_card') {
      this.setState({attributes_enabled: !attributes_enabled});
    }
    else if (name === 'permission_card') {
      this.setState({permission_enabled: !permission_enabled});
    }
  }

  handleAddPosition = (id, event) => {
    let state = !document.getElementById(id).hidden;
    document.getElementById(id).hidden = state;
  }

  handleAddRole = (id, event) => {
    let state = !document.getElementById(id).hidden;
    document.getElementById(id).hidden = state;
  }

  handleSaveRole = (event) => {
    let roleArr = [];
    this.props.appStore.userPermissions.forEach((l, v) => {
      roleArr.push(l.value)
    });
    this.permissionApi.setUserRoles(this.state.user.user_id, roleArr, null).then(function () {
      toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success..."/>, {
        position: toast.POSITION.BOTTOM_RIGHT,
        autoClose: 1000
      });
    })
  }

  handleError = (error) => {
    // TODO finish the function if neded
    console.log('handleError: error=', error)
    toast.error(error, {
      position: toast.POSITION.TOP_LEFT
    });
  }

  handleCreateUser = (event) => {
    let self = this;
    this.state.user.setOrganizationId(this.state.loginUser.getOrganizationId());

    this.userApi.createUser(this.state.user,
      (e) => {
        console.log('Error Create User:', e);
      }).then(function () {
      if (self.props.loadList) {
        self.props.loadList();       // refresh list
      }
      console.log('Save toast');
      toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success..."/>, {
        position: toast.POSITION.BOTTOM_RIGHT,
        autoClose: 1000
      });
    });
  }

  handleSelectChangeRole = (value) => {
    this.props.appStore.userPermissions = value;
  }

  handleSelectChangePosition = (event) => {
    this.setState({userPositions: event});
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "new_email":
        this.state.user.email = event.target.value;
        break;
      case "new_cellPhone":
        this.state.user.cell_phone = event.target.value;
        break;
      case "password":
        this.state.user.password = event.target.value;
        break;
      case "email":
        this.state.user.email = event.target.value;
        break;
      case "whole_name":
        const fio = event.target.value.split(' ')
        if (fio[2])
          this.state.user.middle_name = fio[2];
        if (fio[1])
          this.state.user.first_name = fio[1];
        if (fio[0])
          this.state.user.last_name = fio[0];
        break;
      case "first_name":
        this.state.user.first_name = event.target.value;
        break;
      case "middle_name":
        this.state.user.middle_name = event.target.value;
        break;
      case "last_name":
        this.state.user.last_name = event.target.value;
        break;
      case "nick_name":
        this.state.user.nick_name = event.target.value;
        break;
      case "cell_phone":
        this.state.user.cell_phone = event.target.value;
        break;
      case "home_phone":
        this.state.user.home_phone = event.target.value;
        break;
      case "work_phone":
        this.state.user.work_phone = event.target.value;
        break;
      case "passport_number":
        this.state.user.passport_number = event.target.value;
        break;
      case "date_of_birth":
        this.dateOfBirth = event.target.value;
        break;
      case "employment_date":
        this.employmentDate = event.target.value;
        break;
      case "actual_employment_date":
        this.actualEmploymentDate = event.target.value;
        break;
      case "dismissal_date":
        this.dismissalDate = event.target.value;
        break;
      case "actual_dismissal_date":
        this.actualDismissalDate = event.target.value;
        break;
      case "tax_payer_id":
        this.state.user.setTaxPayerId(event.target.value);
        break;
      case "diploma_number":
        this.state.user.setDiplomaNumber(event.target.value);
        break;
      case "diploma_date":
        this.diplomaDate = event.target.value;
        break;
      case "retirement_id_number":
        this.state.user.retirement_id_number = event.target.value;
        break;
      case "retirement_date":
        this.retirementDate = event.target.value;
        break;
      case "medical_book":
        this.state.user.medical_book = event.target.value;
        break;
      case "medical_book_date":
        this.medicalBookDate = event.target.value;
        break;
      case "employment_book_number":
        this.state.user.employment_book_number = event.target.value;
        break;
      default:
        break;
    }
    this.setState({[event.target.id]: event.target.value});
  }

  handleChangeMobilePhone = (value) => {
    this.setState({mobilePhone: value});
    this.handleChange({target: {id: 'cell_phone', value: value}})
  }
  handleChangeHomePhone = (value) => {
    this.setState({homePhone: value});
    this.handleChange({target: {id: 'home_phone', value: value}})
  }
  handleChangeWorkPhone = (value) => {
    this.setState({workPhone: value});
    this.handleChange({target: {id: 'work_phone', value: value}})
  }

  render() {
    const {personal_info_enabled, attributes_enabled, permission_enabled} = this.state;
    const show = (this.state.user) ? true : false;
    return (
      <div>
        {!this.state.isLoading &&
        <div className="animated fadeIn">
          <Row hidden={this.state.userId === 'new' ? false : true}>
            <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
              <Card id="new_user_card">
                <CardHeader>
                  <button id="save_new_user" onClick={this.handleCreateUser}>
                    <i className="icon-cloud-upload"></i>
                  </button>
                  <strong><FormattedMessage id="users.edit.new_user"
                                            defaultMessage="Create new user"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="new_user_enabled"
                               onClick={(e) => this.formApi.handleFormEnableDisable('new_user_card', e)}
                               className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                               label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                  </div>
                </CardHeader>
                <RegisterForm handleChange={this.handleChange}/>
              </Card>
            </Col>
          </Row>
          <Row hidden={this.state.userId === 'new' ? true : false}>
            <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
              <Card id="email_card">
                <CardHeader>
                  <i><button id="save_email" onClick={this.handleSaveEmail}>
                    <i className="icon-cloud-upload"></i>
                  </button></i>
                  <strong><FormattedMessage id="users.edit.email_management"
                                            defaultMessage="Email management"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="email_management_enabled"
                               onClick={(e) => this.formApi.handleFormEnableDisable('email_card', e)}
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
                          pholder => <Input onChange={this.handleChange} value={this.state.user.email}
                                            type="text" id="email" name="email" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </FormGroup>
                </CardBody>
              </Card>

              <Card id="password_card">
                <CardHeader>
                  <i><button id="save_personal_info" onClick={this.handleSavePassword}>
                    <i className="icon-cloud-upload"></i>
                  </button></i>
                  <strong><FormattedMessage id="users.edit.password_management"
                                            defaultMessage="Password management"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="password_management_enabled"
                               onClick={(e) => this.formApi.handleFormEnableDisable('password_card', e)}
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
                                            type="password" id="mgmt_password" name="mgmt_password"
                                            placeholder={pholder}
                                            required/>
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
                                            type="password" id="mgmt_repeat_password" name="mgmt_repeat_password"
                                            placeholder={pholder}
                                            required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </FormGroup>

                </CardBody>
              </Card>

              <Card id="personal_info_card">
                <CardHeader>
                  <i><button id="save_personal_info" onClick={this.handleSaveUserInfo}>
                    <i className="icon-cloud-upload"></i>
                  </button></i>
                  <strong><FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="personal_info_enabled"
                               onClick={(e) => {
                                 this.handleFormEnableDisable('personal_info_card');
                                 return this.formApi.handleFormEnableDisable('personal_info_card', e)
                               }}
                               className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                               label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                  </div>
                </CardHeader>

                <CardBody>

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
                                            value={this.state.user.first_name}
                                            type="text" id="first_name" name="first_name" placeholder={pholder}
                                            required/>
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
                                            value={this.state.user.middle_name}
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
                                            value={this.state.user.last_name}
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
                                            value={this.state.user.nick_name}
                                            type="text" id="nick_name" name="nick_name" placeholder={pholder} required/>
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
                             value={this.DateOfBirth}
                             id="date_of_birth" name="date_of_birth" placeholder="date"/>
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
                                            value={this.state.user.passport_number}
                                            type="text" id="passport_number" name="passport_number"
                                            placeholder={pholder}
                                            required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </FormGroup>

                  {/*Cell phone*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <FormattedMessage id="users.edit.cellphoneshort" defaultMessage="Cell"/>
                        </InputGroupText>
                      </InputGroupAddon>
                      <span><ReactPhoneInput defaultCountry={country} value={this.state.mobilePhone}
                                             onChange={this.handleChangeMobilePhone}
                                             inputStyle={{width: '100%'}}/></span>
                    </InputGroup>
                  </FormGroup>

                  {/*Home phone*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <FormattedMessage id="users.edit.homephoneshort" defaultMessage="Home"/>
                        </InputGroupText>
                      </InputGroupAddon>
                      <span><ReactPhoneInput defaultCountry={country} value={this.state.homePhone}
                                             onChange={this.handleChangeHomePhone}
                                             inputStyle={{width: '100%'}}/></span>
                    </InputGroup>
                  </FormGroup>

                  {/*Work phone*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <FormattedMessage id="users.edit.homephoneshort" defaultMessage="Work"/>
                        </InputGroupText>
                      </InputGroupAddon>
                      <span><ReactPhoneInput defaultCountry={country} value={this.state.workPhone}
                                             onChange={this.handleChangeWorkPhone}
                                             inputStyle={{width: '100%'}}/></span>
                    </InputGroup>
                  </FormGroup>

                </CardBody>
              </Card>
            </Col>

            <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
              <Card id='attributes_card'>
                <CardHeader>
                  <i><button onClick={this.handleSaveUserInfo}>
                    <i className="icon-cloud-upload"></i>
                  </button></i>
                  <strong><FormattedMessage id="users.edit.personal" defaultMessage="Attributes"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="attributes_enabled"
                               onClick={(e) => {
                                 this.handleFormEnableDisable('attributes_card');
                                 return this.formApi.handleFormEnableDisable('attributes_card', e)
                               }}
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
                      <Input onChange={this.handleChange}
                             type="date" id="employment_date" name="employment_date" placeholder="date"
                             value={this.EmploymentDate}/>
                    </InputGroup>
                  </FormGroup>

                  {/*Actual start date*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actualstartdate" defaultMessage="Start date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input onChange={this.handleChange}
                             type="date" id="actual_employment_date" name="actual_employment_date" placeholder="date"
                             value={this.ActualEmploymentDate}/>
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
                      <Input onChange={this.handleChange}
                             type="date" id="dismissal_date" name="dismissal_date" placeholder="date"
                             value={this.DismissalDate}/>
                    </InputGroup>
                  </FormGroup>
                  {/*Actual last date*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actuallastdate" defaultMessage="Last date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input onChange={this.handleChange}
                             type="date" id="actual_dismissal_date" name="actual_dismissal_date" placeholder="date"
                             value={this.actualDismissalDate}/>
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
                                            value={this.state.user.tax_payer_id}
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
                                            value={this.state.user.diploma_number}
                                            type="text" id="diploma_number" name="diploma_number" placeholder={pholder}
                                            required/>
                        }
                      </FormattedMessage>
                      {/*Diploma date*/}
                      <Input onChange={this.handleChange}
                             type="date" id="diploma_date" name="diploma_date" placeholder="date"
                             value={this.diplomaDate}/>
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
                                            value={this.state.user.retirement_id_number}
                                            type="text" id="retirement_id_number" name="retirement_id_number"
                                            placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                      {/*Diploma date*/}
                      <Input onChange={this.handleChange}
                             type="date" id="retirement_date" name="retirement_date" placeholder="date"
                             value={this.retirementDate}/>
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
                                            value={this.state.user.medical_book}
                                            type="text" id="medical_book" name="medical_book" placeholder={pholder}
                                            required/>
                        }
                      </FormattedMessage>
                      {/*Medical book date*/}
                      <Input onChange={this.handleChange}
                             type="date" id="medical_book_date" name="medical_book_date" placeholder="date"
                             value={this.medicalBookDate}/>
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
                                            value={this.state.user.employment_book_number}
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
                          value={this.state.userPositions}
                          options={this.props.appStore.companyPositions}
                          onChange={this.handleSelectChangePosition}
                          multi
                          disabled={attributes_enabled}
                        />
                      </Col>
                      <button disabled={attributes_enabled}
                              onClick={(e) => this.handleAddPosition('add_position_card', e)}><i
                        className="icon-plus"></i></button>
                    </InputGroup>
                  </FormGroup>
                </CardBody>
              </Card>
              <PositionForm positionApiParent={this.positionApi} readyPosition={this.readyPosition}/>
              <Card id="permission_card">
                <CardHeader>
                  <i><button id="save_permission" onClick={this.handleSaveRole}>
                    <i className="icon-cloud-upload"></i>
                  </button></i>
                  <strong><FormattedMessage id="users.edit.role_management"
                                            defaultMessage="Role management"/></strong>
                  <div className="card-header-actions">
                    <AppSwitch id="permission_management_enabled"
                               onClick={(e) => {
                                 this.handleFormEnableDisable('permission_card');
                                 return this.formApi.handleFormEnableDisable('permission_card', e)
                               }}

                               className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                               label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                  </div>
                </CardHeader>
                <CardBody>
                  {/*Permission*/}
                  <FormGroup row>
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-user-plus"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Col md="10">
                        <Select
                          id="permissions"
                          name="permissions"
                          value={this.props.appStore.userPermissions}
                          options={this.props.appStore.companyPermissions}
                          onChange={this.handleSelectChangeRole}
                          multi
                          disabled={permission_enabled}
                        />
                      </Col>
                    </InputGroup>
                  </FormGroup>
                </CardBody>
              </Card>
              <PermissionForm permissionApiParent={this.permissionApi} readyPermission={this.readyPermission}
                              user={this.state.user}/>

              <AddressForm userId={this.props.appStore.userData.user_id}
                           addressId={this.state.user.address_id}/>
            </Col>
          </Row>
        </div>
        }
        {this.state.isLoading&&
          <Spinner/>
        }
      </div>
    );
  }
}

export default inject("appStore")(observer(EditUser));
