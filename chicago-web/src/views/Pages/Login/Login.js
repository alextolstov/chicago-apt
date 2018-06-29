import React, {Component} from 'react';
import {
  Alert,
  Button,
  Card,
  CardBody,
  CardGroup,
  Col,
  Container,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row
} from 'reactstrap';
import {Link} from 'react-router-dom'
import config from 'react-global-configuration';

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

    if (this.state.email === ""){
      this.handleError("Email can't be empty");
      return;
    }

    if (this.state.password === ""){
      this.handleError("Password can't be empty");
      return;
    }
    // Username must be lower case
    let form = "username=" + this.state.email.toLowerCase() +"&password=" + this.state.password;
    let url = config.get("debug").server_url;

    fetch('/login', {
      method: "POST",
      body: form,
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        this.props.history.push("/dashbord");
      })
      // .then(proto => {
      //   let login_response = usermessages_proto.LoginUserResponse.deserializeBinary(proto);
      //   if (login_response.getTransactionError() !== undefined) {
      //     this.handleError(login_response.getTransactionError().getErrorMessage());
      //   } else {
      //     // Redirect current page to login
      //     this.props.history.push("/dashbord");
      //   }
      // })
      .catch(rest_error => {
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
                    <h1>Login</h1>
                    <p className="text-muted">Sign In to your account</p>
                    <form action="/login" method="post">
                      <InputGroup className="mb-3">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-user"></i>
                          </InputGroupText>
                        </InputGroupAddon>
                        <Input autoFocus
                               value={this.state.email}
                               id="email"
                               onChange={this.handleChange}
                               type="text" name="email" placeholder="Email"/>
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
                      </InputGroup>
                      {this.state.show_error ?
                        <Alert color="danger" id="error_text">{this.state.error_text}</Alert> : ""}
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
                      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut
                        labore et dolore magna aliqua.</p>
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

export default Login;
