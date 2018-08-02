import React, {Component} from 'react';
import {FormattedMessage} from 'react-intl';
import {Card, CardHeader, Col, Row} from 'reactstrap';

class EditUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      caption: props.match.params.newuser ?
        <FormattedMessage id="users.edit.newuser" defaultMessage="Create new user"/> :
        <FormattedMessage id="users.edit.user" defaultMessage="Edit user"/>
    };
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
                {' '}
              </CardHeader>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default EditUser;
