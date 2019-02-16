import React, {Component} from 'react';
import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Col,
  Container,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  FormFeedback,
  Row
} from 'reactstrap';
import {Link} from 'react-router-dom'
import {defineMessages, FormattedMessage} from 'react-intl';
import {AppSwitch} from '@coreui/react'
import ReactPhoneInput from 'react-phone-input-2'
import {country} from "../../../index";

const messages = defineMessages({
  emailPlace: {
    id: 'login.email',
    defaultMessage: 'Email',
  }
});


const user_proto = require('dto/user_pb');
const usermessages_proto = require('dto/usermessages_pb.js');

class Register extends Component {
  constructor(props) {
    super(props);

    this.state = {
      emailOrPhone: "",
      fullname: "",
      password: "",
      repeat_password: "",
      error_text: "",
      show_error: false,
      modePhone: true
    };
  }

  handleToogleMode=() => {
    console.log('handleToogleMode this.state.modePhone= ', this.state.modePhone);

    this.setState({modePhone: !this.state.modePhone});
  }
  handleChangePhone = (value) => {
    this.state.emailOrPhone = value;
    this.setState({phone: value});
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleError(error) {
    this.setState({show_error: true});
    this.setState({error_text: error});
  }

  resetError() {
    this.setState({show_error: false});
    this.setState({error_text: ""});
  }

  handleSubmit = async event => {
    event.stopPropagation();
    event.preventDefault();
    this.resetError();

    if (this.state.emailOrPhone === ""){
      this.handleError("Email can't be empty");
      return;
    }

    if (this.state.fullname === ""){
      this.handleError("User name can't be empty");
      return;
    }

    if (this.state.password === ""){
      this.handleError("Password can't be empty");
      return;
    }

    if (this.state.password !== this.state.repeat_password){
      this.handleError("Password not match");
      return;
    }

    let user=new user_proto.User();
    
    // Username must be lower case
    user.setEmail(this.state.emailOrPhone.toLowerCase());
    user.setCellPhone(this.state.phone);
    // Parse full name
    let parts = this.state.fullname.split(" ");
    user.setFirstName(parts[0]);
    if (parts.length === 2) {
      user.setLastName(parts[1]);
    } else if (parts.length === 3) {
      user.setMiddleName(parts[1]);
      user.setLastName(parts[2]);
    }
    user.setPassword(this.state.password);
    let serialized_user = user.serializeBinary();

    fetch('/api/users/createadmin', {
      method: "POST",
      body: serialized_user,
      headers: {
        'Content-Type': 'application/octet-stream'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        return response.arrayBuffer();
      })
      .then(proto => {
        let user_response = usermessages_proto.UserResponse.deserializeBinary(proto);
        if (user_response.getTransactionError() !== undefined) {
          this.handleError(user_response.getTransactionError().getErrorMessage());
        } else {
          // Redirect current page to login
          this.props.history.push("/login");
        }
      })
      .catch(rest_error => {
        if (rest_error.status === 500) {
          // Redirect current page to login
          this.handleError("Error 500. Server error.");
          return;
        }
        rest_error.json().then(errorMessage => {
          this.handleError(errorMessage);
        })
      })
  }

  render() {
    return (
      <div className="app flex-row align-items-center">
        <Container>
          <Row className="justify-content-center">
            <Col md="6">
              <Card className="mx-4">
                <CardBody className="p-4">
                  <h1>Register</h1>
                  <p className="text-muted">Create your account</p>
                  <form action="/user/create" method="post">
                      <Row>
                        <div class="col-sm-3">
                            <AppSwitch id="phoneOrMailId" onClick={this.handleToogleMode}
                                 className={'mx-1'} color={'dark'} outline={'alt'} checked={true}
                                 label dataOn={'\u260E'} dataOff={'@'} size={'lg'}/>
                        </div>
                        <div class="col-sm-9">
                             {this.state.modePhone&&
                              <InputGroup className="mb-3">
                                <ReactPhoneInput defaultCountry={country} value={this.state.phone}
                                                onChange={this.handleChangePhone}  inputStyle={{width: '100%'}}/>
                              </InputGroup>
                             }
                             {!this.state.modePhone &&
                              <InputGroup className="mb-3">
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
                        </div>
                      </Row>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input id="fullname"
                             value={this.state.fullname}
                             onChange={this.handleChange}
                             type="text" name="fullname" placeholder="First/Middle/Last Name"/>
                    </InputGroup>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-lock"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input id="password"
                             value={this.state.password}
                             onChange={this.handleChange}
                             type="password" placeholder="Password"/>
                    </InputGroup>
                    <InputGroup className="mb-4">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-lock"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input id="repeat_password"
                             value={this.state.repeat_password}
                             onChange={this.handleChange}
                             type="password" placeholder="Repeat password"/>
                      <Input hidden invalid />
                      <FormFeedback>{this.state.error_text}</FormFeedback>
                    </InputGroup>
                    <Button onClick={this.handleSubmit}
                            color="success" block type="submit">Create Account</Button>
                    <br/>
                    <Link to="/login">
                      <Button color="success" block>To Login Page</Button>
                    </Link>
                  </form>
                </CardBody>
                <CardFooter className="p-4">
                  <Row>
                    <Col xs="12" sm="6">
                      <Button className="btn-facebook" block><span>facebook</span></Button>
                    </Col>
                    <Col xs="12" sm="6">
                      <Button className="btn-twitter" block><span>twitter</span></Button>
                    </Col>
                  </Row>
                </CardFooter>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Register;
