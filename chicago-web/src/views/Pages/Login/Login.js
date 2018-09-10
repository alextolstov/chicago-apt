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
import {Link} from 'react-router-dom'
import {defineMessages, FormattedMessage} from 'react-intl';
import config from 'react-global-configuration';
import {observer, inject} from 'mobx-react';

const user_proto = require('models/user_pb');
const usermessages_proto = require('models/usermessages_pb.js');

// Localization for place holders
const messages = defineMessages({
  emailPlace: {
    id: 'login.email.placeholder',
    defaultMessage: 'Email',
  }
});

class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      email: "",
      password: "",
      error_text: "",
      show_error: false
    };
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

  handleSubmit = event => {
    event.stopPropagation();
    event.preventDefault();
    this.resetError();

    if (this.state.email === "") {
      this.handleError("Email can't be empty");
      return;
    }

    if (this.state.password === "") {
      this.handleError("Password can't be empty");
      return;
    }
    // Username must be lower case
    let form = "username=" + this.state.email.toLowerCase() + "&password=" + this.state.password;
    let url = config.get("debug").server_url;

    this.login(form);
  }

  login(credentials) {
    fetch('/login', {
      method: "POST",
      body: credentials,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        this.getUser();
      })
      .catch(rest_error => {
        if (rest_error.status == 401) {
          this.handleError("User unauthorized");
        } else if (rest_error.status == 404) {
          this.handleError("Error 404. Page not found.");
        } else if (rest_error.status == 500) {
          this.handleError("Error 500. Server error.");
        } else {
          rest_error.json().then(errorMessage => {
            this.handleError(errorMessage);
          })
        }
      })
  }

  getUser() {
    let user = new user_proto.User();
    // Username must be lower case
    user.setEmail(this.state.email.toLowerCase());
    let serialized_user = user.serializeBinary();

    fetch('/api/users/user', {
      method: "POST",
      body: serialized_user,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.arrayBuffer();
    }).then(proto => {
      let user_response = usermessages_proto.UserResponse.deserializeBinary(proto);
      if (user_response.getTransactionError() !== undefined) {
        this.handleError(user_response.getTransactionError().getErrorMessage());
      } else {
        // AppStore.permissions = ['perm1', 'perm2'];
        this.props.appStore.userData = user_response.getUser();
        window.sessionStorage.setItem("current_user", this.props.appStore.userData.getUserId());
        // Redirect current page to dashboard
        this.props.history.push("/dashbord");
      }
    }).catch(rest_error => {
      if (rest_error.status == 500) {
        // Show error
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
            <Col md="8">
              <CardGroup>
                <Card className="p-4">
                  <CardBody>
                    <h1><FormattedMessage id="login.short.title" defaultMessage="Login"/></h1>
                    <p className="text-muted"><FormattedMessage id="login.long.title"
                                                                defaultMessage="Sign In to your account"/></p>
                    <form action="/login" method="post">
                      <InputGroup className="mb-3">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-user"></i>
                          </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.emailPlace}>
                          {
                            pholder => <Input autoFocus
                                              value={this.state.email}
                                              id="email"
                                              onChange={this.handleChange}
                                              type="text" name="email" placeholder={pholder}/>
                          }
                        </FormattedMessage>
                      </InputGroup>
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
