import React, {Component} from 'react';
import {FormattedMessage} from 'react-intl';

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
        <h1>{this.state.caption}</h1>
      </div>
    );
  }
}

export default EditUser;
