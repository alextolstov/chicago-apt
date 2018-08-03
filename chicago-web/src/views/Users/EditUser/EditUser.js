import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
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

const messages = defineMessages({
  firstNamePlace: {
    id: 'users.edit.firstname.placeholder',
    defaultMessage: 'First Name'
  },
  midleNamePlace: {
    id: 'users.edit.midlename.placeholder',
    defaultMessage: 'Midle Name'
  },
  lastNamePlace: {
    id: 'users.edit.lastname.placeholder',
    defaultMessage: 'Last Name'
  },
  emailPlace: {
    id: 'users.edit.email.placeholder',
    defaultMessage: 'Email'
  },
  nickNamePlace: {
    id: 'users.edit.nickname.placeholder',
    defaultMessage: 'Nickname'
  }
});

class EditUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      caption: props.match.params.newuser ?
        <FormattedMessage id="users.edit.newuser" defaultMessage="Create new user"/> :
        <FormattedMessage id="users.edit.user" defaultMessage="Edit user"/>
    };
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
                <strong>{this.state.caption}</strong>
              </CardHeader>
              <CardBody>
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.dob" defaultMessage="DOB"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input" name="date-input" placeholder="date" />
                    </InputGroup>
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="12">
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
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="12">
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
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="12">
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
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="12">
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
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="12">
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
                  </Col>
                </FormGroup>

                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.passport" defaultMessage="Passport #"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" name="passport"/>
                    </InputGroup>
                  </Col>
                </FormGroup>

              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default EditUser;
