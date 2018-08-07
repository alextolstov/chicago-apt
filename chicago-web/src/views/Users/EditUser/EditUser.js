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
  taxPayerPlace: {
    id: 'users.edit.taxpayerid',
    defaultMessage: 'Tax payer id'
  }
});

class EditUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      caption: props.match.params.newuser ?
        <FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/> :
        <FormattedMessage id="users.edit.user" defaultMessage="Edit employee"/>
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
                <strong><FormattedMessage id="users.edit.personal" defaultMessage="Personal Information"/></strong>
              </CardHeader>

              <CardBody>
                {/*Email*/}
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

                {/*First name*/}
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

                {/*Middle name*/}
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

                {/*Last name*/}
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

                {/*Nickname*/}
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

                {/*Passport*/}
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

                {/* DOB */}
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
                      <Input type="date" id="date-input" name="date-input" placeholder="date"/>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Home phone*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-phone"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.homePhonePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="homephone" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Cell phone*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-phone"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.cellPhonePlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="cellphone" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </Col>
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
                {/*Tax payer id*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="fa fa-male"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <FormattedMessage {...messages.taxPayerPlace}>
                        {
                          pholder => <Input onChange={this.handleChange}
                                            type="text" name="taxpayerid" placeholder={pholder} required/>
                        }
                      </FormattedMessage>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Employment date*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.employmentdate" defaultMessage="Employment date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-employment" name="date-input-employment" placeholder="date"/>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Actual start date*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actualstartdate" defaultMessage="Actual start date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-actualstart" name="date-input-actualstart" placeholder="date"/>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Dismissal date*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.dismissaldate" defaultMessage="Dismissal date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-dismissal" name="date-input-dismissal" placeholder="date"/>
                    </InputGroup>
                  </Col>
                </FormGroup>

                {/*Actual last date*/}
                <FormGroup row>
                  <Col md="12">
                    <InputGroup>
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i>
                            <FormattedMessage id="users.edit.actuallastdate" defaultMessage="Actual last date"/>
                          </i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="date" id="date-input-actuallast" name="date-input-actuallast" placeholder="date"/>
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
