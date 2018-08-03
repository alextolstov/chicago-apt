import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {
  Row, Col, Card, CardHeader, CardBody, FormGroup, Label, FormText, Input,
  InputGroup, InputGroupAddon, InputGroupText
} from 'reactstrap';
import {TextMask, InputAdapter} from 'react-text-mask-hoc';

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
                  <Col md="3">
                    <Label htmlFor="firstname">
                      <FormattedMessage id="users.edit.firstname" defaultMessage="First Name"/> :
                    </Label>
                  </Col>
                  <Col xs="12" md="9">
                    <FormattedMessage {...messages.firstNamePlace}>
                      {
                        pholder => <Input onChange={this.handleChange}
                                          type="text" name="firstname" placeholder={pholder} required/>
                      }
                    </FormattedMessage>
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                  <Label htmlFor="midlename">
                    <FormattedMessage id="users.edit.midletname" defaultMessage="Midle Name"/> :
                  </Label>
                  </Col>
                  <Col xs="12" md="9">
                  <FormattedMessage {...messages.midleNamePlace}>
                    {
                      pholder => <Input onChange={this.handleChange}
                                        type="text" name="midlename" placeholder={pholder} required/>
                    }
                  </FormattedMessage>
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                  <Label htmlFor="lastname">
                    <FormattedMessage id="users.edit.lastname" defaultMessage="Last Name"/> :
                  </Label>
                  </Col>
                  <Col xs="12" md="9">
                  <FormattedMessage {...messages.lastNamePlace}>
                    {
                      pholder => <Input onChange={this.handleChange}
                                        type="text" name="lastname" placeholder={pholder} required/>
                    }
                  </FormattedMessage>
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
