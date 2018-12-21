import React, {Component} from 'react';
import {
  Button,
  Card,
  CardBody,
  CardGroup,
  Col,
  Container,
  FormFeedback,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row
} from 'reactstrap';
import {AppSwitch} from '@coreui/react'

import {Link} from 'react-router-dom'
import {defineMessages, FormattedMessage} from 'react-intl';
import {inject} from 'mobx-react';
import UserApi from '../../../api/UserApi';
import PermissionApi from '../../../api/PermissionApi';
import ReactPhoneInput from 'react-phone-input-2'

// Localization for place holders
const messages = defineMessages({
  emailPlace: {
    id: 'login.email',
    defaultMessage: 'Email',
  }
});


class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      emailOrPhone: "",
      password: "",
      error_text: "",
      show_error: false,
      userApi: new UserApi(),
      permissionApi: new PermissionApi(),
      permissionsArr: [],
      permissionsUserArr: [],
      phone: "",
      modePhone: true
    };
  }

  handleChangePhone = (value) => {
    this.state.emailOrPhone = value;
    this.setState({phone: value});
  }

  handleToogleMode = () => {
    console.log('handleToogleMode this.state.modePhone= ', this.state.modePhone);

    this.setState({modePhone: !this.state.modePhone});
  }


  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleError = error => {
    this.setState({show_error: true});
    this.setState({error_text: error});
  }

  resetError = () => {
    this.setState({show_error: false});
    this.setState({error_text: ""});
  }

  handleSubmit = event => {
    event.stopPropagation();
    event.preventDefault();
    this.resetError();

    if (this.state.emailOrPhone === "") {
      this.handleError("Phone or Email can't be empty");
      return;
    }

    if (this.state.password === "") {
      this.handleError("Password can't be empty");
      return;
    }

    // Andrey's conversation PhoneNumber
    console.log('!!!!', this.state.emailOrPhone);

    //   this.state.emailOrPhone=convertPhoneNumber(this.state.emailOrPhone);
    // Username must be lower case
    let form = "username=" + this.state.emailOrPhone.toLowerCase().replace('+', '%2B') + "&password=" + this.state.password;

    let self = this;

    this.state.userApi.login(form, this.state.emailOrPhone.toLowerCase(), this.handleError).then(function (data) {
      if (data != null) {
        let user = data.getUser();
        self.props.appStore.userData = user;
        window.sessionStorage.setItem("current_user", user.getUserId());
        // get Roles/Permission
        self.state.permissionApi.setPermissionsUser(self.props.appStore, user, () => self.props.history.push("/dashbord"));
      }
    })
  }

  render() {
    return (
      <div className="app flex-row align-items-center">
        <Container>
          <Row className="justify-content-center">
            <Col md="8">
              <CardGroup>
                <Card className="p-4">
                  <CardBody>
                    <p className="text-muted"><FormattedMessage id="login.long.title"
                                                                defaultMessage="Sign In to your account"/></p>
                    <h1>
                      <AppSwitch id="phoneOrMailId"
                                 onClick={this.handleToogleMode}
                                 className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                                 label dataOn={'\u260E'} dataOff={'@'} size={'lg'}/>
                      <FormattedMessage id="login.short.title" defaultMessage="Login"/>
                    </h1>
                    <form action="/login" method="post">
                      {this.state.modePhone &&
                      <InputGroup className="mb-3">
                        <ReactPhoneInput defaultCountry={'ru'} value={this.state.phone}
                                         onChange={this.handleChangePhone}/>
                      </InputGroup>
                      }
                      {!this.state.modePhone &&
                      <InputGroup className="mb-3">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-user"></i>
                          </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.emailPlace}>
                          {
                            pholder => <Input autoFocus
                                              value={this.state.emailOrPhone}
                                              id="emailOrPhone"
                                              onChange={this.handleChange}
                                              type="text" name="email" placeholder={pholder}/>
                          }
                        </FormattedMessage>
                      </InputGroup>
                      }
                      <InputGroup className="mb-4">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-lock"></i>
                          </InputGroupText>
                        </InputGroupAddon>
                        <Input value={this.state.password}
                               id="password"
                               onChange={this.handleChange}
                               type="password" name="password" placeholder="Password"/>
                        <Input hidden invalid/>
                        <FormFeedback>{this.state.error_text}</FormFeedback>
                      </InputGroup>
                      <Row>
                        <Col xs="6">
                          <Button onClick={this.handleSubmit}
                                  color="primary" className="px-4" type="submit">Login</Button>
                        </Col>
                        <Col xs="6" className="text-right">
                          <Button color="link" className="px-0">Forgot password?</Button>
                        </Col>
                      </Row>
                    </form>
                  </CardBody>
                </Card>
                <Card className="text-white bg-primary py-5 d-md-down">
                  <CardBody className="text-center">
                    <div>
                      <h2>Sign up</h2>
                      <p>Just click button below to create your company account!</p>
                      <Link to="/register">
                        <Button color="primary" className="mt-3" active>Register Now!</Button>
                      </Link>
                    </div>
                  </CardBody>
                </Card>
              </CardGroup>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}


export default inject("appStore")(Login);
