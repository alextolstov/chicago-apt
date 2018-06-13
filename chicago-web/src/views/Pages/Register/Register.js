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
  Row
} from 'reactstrap';
import {Link} from 'react-router-dom'

var user_models = require('models/user_pb');

class Register extends Component {
  constructor(props) {
    super(props);

    this.state = {
      email: "",
      fullname: "",
      password: "",
      repeate_password: ""
    };
  }


  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleSubmit = async event => {
    event.stopPropagation();
    event.preventDefault();

    let new_user = new user_models.User();
    new_user.setEmail(this.state.email);
    // Parse full name
    let parts = this.state.fullname.split(" ");
    new_user.setFirstname(parts[0]);
    if (parts.length == 2) {
      new_user.setLastname(parts[1]);
    } else if (parts.length == 3) {
      new_user.setMiddlename(parts[1]);
      new_user.setLastname(parts[2]);
    }
    new_user.setPassword(this.state.password);
    let bin_user = new_user.serializeBinary();

    fetch('http://localhost:8080/api/users/create', {
      method: "POST",
      body: bin_user,
      mode: 'cors',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Credentials': 'true',
        'Content-Type': 'application/octet-stream'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw response;
        }
        return response.json();
      })
      .then(json => {
        console.log(json);
        // Redirect current page to login
        this.props.history.push("/login");
      })
      .catch(err => {
        err.json().then(errorMessage => {
          console.log(errorMessage);
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
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>@</InputGroupText>
                      </InputGroupAddon>
                      <Input autoFocus
                             id="email"
                             value={this.state.email}
                             onChange={this.handleChange}
                             type="text" placeholder="Email (as username)"/>
                    </InputGroup>
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
                      <Input id="repeate_password"
                             value={this.state.repeate_password}
                             onChange={this.handleChange}
                             type="password" placeholder="Repeat password"/>
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
