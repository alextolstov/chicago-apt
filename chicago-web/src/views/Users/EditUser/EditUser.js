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
import {ToastContainer, toast} from 'react-toastify';
import convertPhoneNumber from '../../Pages/Login/convertPhoneNumber';

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
  },
  role: {
    id: 'users.edit.role',
    defaultMessage: 'Role'
  }

});

const isEmailAddress=(strEmailPhone) => {
  if(strEmailPhone.indexOf('@')!==-1) {
    console.log('isEmailAddress: ', strEmailPhone, ' is true');
    return true;
  }
  console.log('isEmailAddress: ', strEmailPhone, ' is false');
  return false;
}

const toPhoneFormat=(strEmailPhone) => {
  if(strEmailPhone.length===10)
    return '+7 ('+strEmailPhone.substr(0, 3)+')'+strEmailPhone.substr(3, 3)+
      ' '+strEmailPhone.substr(6, 2)+' '+strEmailPhone.substr(8, 2);
  else
    return strEmailPhone;
}

class EditUser extends Component {
  constructor(props) {
    super(props);

    this.state = {
      dateTimeApi: new DateTimeApi(),
      userApi: new UserApi(),
      positionApi: new PositionApi(),
      permissionApi: new PermissionApi(),
      formApi: new FormApi(),

      userPositions: [],
      userPermissions: [],
      userRoles: [],

      userId: (props.match && props.match.params) ? props.match.params.id : props.userId,
      user: "",
      loginUser: null,

      readyPosition : false,
      readyPermission: false,
      personal_info_enabled: false,
      attributes_enabled: false,
      permission_enabled: false,

      need_show : false,
    
    };
    this.phoneOrEmail='';
    this.readyPosition = this.readyPosition.bind(this);
    this.readyPermission = this.readyPermission.bind(this);
    this.handleFormEnableDisable = this.handleFormEnableDisable.bind(this);
    this.handleChange=this.handleChange.bind(this);
    this.handleCreateUser=this.handleCreateUser.bind(this);

    this.state.loginUser = jspb.Message.cloneMessage(this.props.appStore.userData);
    if (this.state.userId === 'new') {
      this.state.user = new user_proto.User();
    }
    else if (this.state.userId === 'current') {
      this.state.user = jspb.Message.cloneMessage(this.props.appStore.userData);
      this.state.userPositions = this.state.user.getPositionsList();
    }
  }

  setUncheckedState = (mode) => {
    if (document.getElementById('email_management_enabled').checked === mode) {
      document.getElementById('email_management_enabled').click();
    }
    if (document.getElementById('permission_management_enabled').checked === mode) {
      document.getElementById('permission_management_enabled').click();
    }
    if (document.getElementById('password_management_enabled').checked === mode) {
      document.getElementById('password_management_enabled').click();
    }
    if (document.getElementById('personal_info_enabled').checked  === mode) {
      document.getElementById('personal_info_enabled').click();
    }
    if (document.getElementById('attributes_enabled').checked  === mode) {
      document.getElementById('attributes_enabled').click();
    }
    if (document.getElementById('address_enabled').checked  === mode) {
      document.getElementById('address_enabled').click();
    }
    
  }
  
  readyPosition = () => {
    this.setState({readyPosition : true});
  }

  readyPermission  = () => {
    this.setState({readyPermission : true});
  }

  componentDidMount() {
    console.log('!!!EditUser:componentDidMount');
    
    if (this.state.userId === 'current') {
      this.setUncheckedState( true);
    }
    else
    if (this.state.userId === 'new') {
      this.setUncheckedState( false);
    }
    // get User
    this.state.loginUser = jspb.Message.cloneMessage(this.props.appStore.userData);
    if (this.state.userId === 'new') {
      this.state.user = new user_proto.User();
    }
    else if (this.state.userId === 'current') {
      this.state.user = jspb.Message.cloneMessage(this.props.appStore.userData);
      this.state.userPositions = this.state.user.getPositionsList();
    }
    else {
      let self=this;
      self.state.readyPermission=false;
      this.state.userApi.getUserById( this.state.userId, null).then(function (userMsg) {
      if (userMsg != null) {
          self.state.user=userMsg.getUser();
console.log('EditUser componentDidMount self.state.user ', self.state.user);
        
          self.state.userPositions=self.state.user.getPositionsList();
          self.state.permissionApi.setPermissionsUser(self.props.appStore, self.state.user, self.readyPermission); 

        }
      })
    }
  }

 
  componentDidUpdate(prevProps, prevState, prevContext) {
    if( this.state.need_show === true) {
      this.state.need_show = false;
      let user = null;
      let self = this;
      self.state.readyPermission=false;
      this.state.userApi.getUserById( this.state.userId, null).then(function (userMsg) {

      if (userMsg != null) {
        user=userMsg.getUser();
console.log('EditUser componentDidUpdate self.state.user ', self.state.user);
        const posList=user.getPositionsList();
console.log('EditUser componentDidUpdate posList', posList);

        self.state.permissionApi.setPermissionsUser(self.props.appStore, user, self.readyPermission); 
        self.setState({user:user, userPositions:user.getPositionsList(), need_show :false});
      }
      })
    }  
  
 }

  static getDerivedStateFromProps(nextProps, prevState) {
    if (nextProps && nextProps.match && nextProps.match.params) {
      if(nextProps.match.params.id !== prevState.userId)
        return {userId: nextProps.match.params.id};
    }
    if( nextProps.userId !== 'current'  && nextProps.userId !== prevState.userId) {
      console.log('getDerivedStateFromProps userId=', nextProps.userId, prevState.userId );
      return {userId: nextProps.userId, need_show: true };
    }    
    return null;
  }
  
  handleFormEnableDisable = (name) => {
     const {personal_info_enabled, attributes_enabled, permission_enabled} = this.state;
     if(name === 'personal_info_enabled') {
       this.setState({personal_info_enabled : !personal_info_enabled});
     }  
     else if( name ===  'attributes_card') {
       this.setState({attributes_enabled : !attributes_enabled});
     }
     else if( name ===  'permission_card') {
      this.setState({permission_enabled : !permission_enabled});
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

  handleSaveUserInfo = (event) => {
    let self = this;
    if (this.state.userId === 'new') {
      this.state.userApi.createUser(this.state.user, this.handleError);
      toast.success(<FormattedMessage id="users.edit.success" defaultMessage="SuccessNew..."/>, {
        position: toast.POSITION.BOTTOM_RIGHT,
        autoClose: 1000 
      });    

    } else {// Working with existing profile
      let positionsArr = [];
      this.state.userPositions.forEach((l, v) => {positionsArr.push(l.value)});
console.log('save setPositionsList positionsArr=', positionsArr);
console.log('save setPositionsList user=', this.state.user);
      
      this.state.user.setPositionsList(positionsArr);
      this.state.userApi.saveUser(this.state.user, this.handleError).then(function () {
        if (self.state.userId === 'current') {
          self.props.appStore.userData = self.state.user;
          self.setUncheckedState(true);
        }
        if(self.props.loadList)
           self.props.loadList();       // refresh list
        console.log('Save toast');
        toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success..."/>, {
          position: toast.POSITION.BOTTOM_RIGHT,
          autoClose: 1000 
        });    
  
      });
    }
  }

  handleSaveRole = (event) => {
    let roleArr = [];
    this.props.appStore.userPermissions.forEach((l, v) => { roleArr.push(l.value) });
    this.state.permissionApi.saveUserRoles(this.state.user, roleArr, null).then(function () {
      toast.success(<FormattedMessage id="users.edit.success" defaultMessage="Success..."/>, {
        position: toast.POSITION.BOTTOM_RIGHT,
        autoClose: 1000 
      });    
    })

  }



  handleError = (error) => {
    // TODO finish the function if neded
    console.log('handleError: error=', error)
  }

  handleCreateUser=(event) => {
    let self=this;
    this.state.user.setOrganizationId(this.state.loginUser.getOrganizationId());

    if( isEmailAddress(this.phoneOrEmail))
       this.state.user.setEmail(this.phoneOrEmail);
    else
       this.state.user.setCellPhone(toPhoneFormat(this.phoneOrEmail));
 /////////////Добавить/////////////////////////////////////////////////////////////////////
    this.state.user.setUserLogin(this.phoneOrEmail);    
 /////////////////////////////////////////////////////////////////////////////////////////   
    this.state.userApi.createUser(this.state.user,
                                  (e) => { console.log('Error Create User:', e);
    }).then(function () {
    if(self.props.loadList)
       self.props.loadList();       // refresh list
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
    this.setState({userPositions:event});
  }

  handleChange = (event) => {
    switch (event.target.id) {
      case "new_email":
          // Andrey's conversation PhoneNumber
        console.log( 'handle change!!!!', event.target.value);
        this.phoneOrEmail=convertPhoneNumber(event.target.value);
        break;
      case "password":
        this.state.user.setPassword(event.target.value);
      break;
      case "email":
        this.state.user.setEmail(event.target.value);
        break;
      case "whole_name":
        const fio=event.target.value.split(' ')
        if( fio[2])
          this.state.user.setMiddleName(fio[2]);
        if( fio[1])
          this.state.user.setFirstName(fio[1]);
        if( fio[0])
          this.state.user.setLastName(fio[0]);
        break;
      case "first_name":
        this.state.user.setFirstName(event.target.value);
        break;
      case "middle_name":
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
        this.state.user.setDateOfBirth(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "employment_date":
        this.state.user.setEmploymentDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "actual_employment_date":
        this.state.user.setActualEmploymentDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "dismissal_date":
        this.state.user.setDismissalDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "actual_dismissal_date":
        this.state.user.setActualDismissalDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "tax_payer_id":
        this.state.user.setTaxPayerId(event.target.value);
        break;
      case "diploma_number":
        this.state.user.setDiplomaNumber(event.target.value);
        break;
      case "diploma_date":
        this.state.user.setDiplomaDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "retirement_id_number":
        this.state.user.setRetirementIdNumber(event.target.value);
        break;
      case "retirement_date":
        this.state.user.setRetirementDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "medical_book":
        this.state.user.setMedicalBook(event.target.value);
        break;
      case "medical_book_date":
        this.state.user.setMedicalBookDate(this.state.dateTimeApi.dateToUnixUTC(event.target.value));
        break;
      case "employment_book_number":
        this.state.user.setEmploymentBookNumber(event.target.value);
        break;
      default:
        break;   
    }
    this.setState({[event.target.id]: event.target.value});
  }

  render() {
    const {personal_info_enabled, attributes_enabled, permission_enabled} = this.state;
    const show = (this.state.user) ? true: false;
    return (
      <div>
      {show &&
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
                             onClick={(e) => this.state.formApi.handleFormEnableDisable('new_user_card', e)}
                             className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                             label dataOn={'\u2713'} dataOff={'\u2715'} size={'sm'}/>
                </div>
              </CardHeader>
              <RegisterForm handleChange = {this.handleChange}/>
            </Card>
          </Col>
        </Row>
        <Row hidden={this.state.userId === 'new' ? true : false}>
          <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
            <Card id="email_card">
              <CardHeader>
                <button id="save_email" onClick={this.handleSaveEmail}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.email_management"
                                          defaultMessage="Email management"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="email_management_enabled"
                             onClick={(e) => this.state.formApi.handleFormEnableDisable('email_card', e)}
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
              </CardBody>
            </Card>

            <Card id="password_card">
              <CardHeader>
                <button id="save_personal_info" onClick={this.handleSavePassword}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.password_management"
                                          defaultMessage="Password management"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="password_management_enabled"
                             onClick={(e) => this.state.formApi.handleFormEnableDisable('password_card', e)}
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
                                          type="password" id="mgmt_password" name="mgmt_password" placeholder={pholder}
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
                <button id="save_personal_info" onClick={this.handleSaveUserInfo}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="personal_info_enabled"
                             onClick={(e) => { this.handleFormEnableDisable('personal_info_card');  
                                               return this.state.formApi.handleFormEnableDisable('personal_info_card', e) }}
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
                                          value={this.state.user.getFirstName === undefined ? "" : this.state.user.getFirstName()}
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
                                          value={this.state.user.getMiddleName === undefined ? "" : this.state.user.getMiddleName()}
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
                                          value={this.state.user.getLastName === undefined ? "" : this.state.user.getLastName()}
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
                                          value={this.state.user.getNickName === undefined ? "" : this.state.user.getNickName()}
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
                           defaultValue={this.state.user.getDateOfBirth !== undefined ? "" : new Date(this.state.user.getDateOfBirth()).toISOString().substr(0, 10)}
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
                                          value={this.state.user.getPassportNumber === undefined ? "" : this.state.user.getPassportNumber()}
                                          type="text" id="passport_number" name="passport_number" placeholder={pholder}
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
                        <i className="fa fa-phone"></i>
                      </InputGroupText>
                    </InputGroupAddon>
                    <FormattedMessage {...messages.cellPhonePlace}>
                      {
                        pholder => <Input onChange={this.handleChange}
                                          value={this.state.user.getCellPhone === undefined ? "" : this.state.user.getCellPhone()}
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
                                          value={this.state.user.getHomePhone === undefined ? "" : this.state.user.getHomePhone()}
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
                                          value={this.state.user.getWorkPhone === undefined ? "" : this.state.user.getWorkPhone()}
                                          type="text" id="work_phone" name="work_phone" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                  </InputGroup>
                </FormGroup>

              </CardBody>
            </Card>
          </Col>

          <Col sm={12} md={6} style={{flexBasis: 'auto'}}>
            <Card id='attributes_card'>
              <CardHeader>
                <button  onClick={this.handleSaveUserInfo}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Attributes"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="attributes_enabled" 
                              onClick={(e) => { this.handleFormEnableDisable('attributes_card');  
                                  return this.state.formApi.handleFormEnableDisable('attributes_card', e) }}
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
                                          value={this.state.user.getTaxPayerId === undefined ? "" : this.state.user.getTaxPayerId()}
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
                                          value={this.state.user.getDiplomaNumber === undefined ? "" : this.state.user.getDiplomaNumber()}
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
                                          value={this.state.user.getRetirementIdNumber === undefined ? "" : this.state.user.getRetirementIdNumber()}
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
                                          value={this.state.user.getMedicalBook === undefined ? "" : this.state.user.getMedicalBook()}
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
                                          value={this.state.user.getMedicalBook === undefined ? "" : this.state.user.getMedicalBook()}
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
                        disabled = {attributes_enabled}
                      />
                    </Col>
                    <button disabled = {attributes_enabled}  onClick={(e) => this.handleAddPosition('add_position_card', e)}><i
                      className="icon-plus"></i></button>
                  </InputGroup>
                </FormGroup>
              </CardBody>
            </Card>
            <PositionForm positionApiParent={this.state.positionApi}  readyPosition={this.readyPosition}/>
            <Card id="permission_card">
              <CardHeader>
                <button id="save_permission" onClick={this.handleSaveRole}>
                  <i className="icon-cloud-upload"></i>
                </button>
                <strong><FormattedMessage id="users.edit.role_management"
                                          defaultMessage="Role management"/></strong>
                <div className="card-header-actions">
                  <AppSwitch id="permission_management_enabled"
                             onClick={(e) => { this.handleFormEnableDisable('permission_card');  
                                               return this.state.formApi.handleFormEnableDisable('permission_card', e) }}

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
                        disabled = {permission_enabled}
                      />
                    </Col>
                  </InputGroup>
                </FormGroup>
              </CardBody>
            </Card>
            <PermissionForm permissionApiParent={this.state.permissionApi}  readyPermission={this.readyPermission} user={this.state.user}/>

            <AddressForm userId={this.props.appStore.userData.getUserId()}
                         addressId={this.state.user.getAddressId === undefined ? "" : this.state.user.getAddressId()}/>
          </Col>
        </Row>
      </div>
      }
      </div>
    );
  }
}

export default inject("appStore")(observer(EditUser));
