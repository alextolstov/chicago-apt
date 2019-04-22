import React, {Component} from 'react';
import {inject, observer} from "mobx-react/index";
import {defineMessages, FormattedMessage} from 'react-intl';
import {
  Card,
  CardBody,
  CardHeader,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Table,
} from 'reactstrap';

const messages = defineMessages({
  positionPlace: {
    id: 'users.edit.newpermission',
    defaultMessage: 'New permission'
  }
});

class PermissionForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      permissionApi: props.permissionApiParent,
      organizationId: this.props.appStore.userData.organization_id,
      permissionsArr: [],
      permissionsUserArr: [],
      permissionsMap: new Map()
    };
  }


  handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      let self = this;
      let val = event.target.value;
      event.target.value = "";

      this.state.permissionApi.createPermission(this.state.organizationId, val, null)
        .then(function (permission) {
          if (permission !== undefined && permission !== null) {
            self.props.appStore.companyPermissions.push({
              value: permission.permission_id,
              label: permission.description
            });
            self.state.permissionsArr.push([permission.permission_id, permission.description]);
            self.setState({permissionsArr: self.state.permissionsArr});
          }
        });
    }
  }

  render() {
    let table = this.state.permissionsArr.map((r) => {
      return <tr key={r[0]}>
        <td>
          <Input id={r[0]} onChange={this.handleChange} bsSize="sm" className="input-sm" defaultValue={r[1]}
                 type="text"/>
        </td>
        <td>
          <i className="cui-delete icons font-2xl"></i>
        </td>
      </tr>
    });

    return (
      <Card id='add_permission_card' hidden>
        <CardHeader>
          <button onClick={this.saveNewPosition}>
            <i className="icon-cloud-upload"></i>
          </button>
          <strong><FormattedMessage id="users.edit.newpermission" defaultMessage="Type new permission and press Enter"/></strong>
        </CardHeader>
        <CardBody>
          {/*Position*/}
          <FormGroup row>
            <InputGroup>
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <i className="icon-star"></i>
                </InputGroupText>
              </InputGroupAddon>
              <FormattedMessage {...messages.positionPlace}>
                {
                  pholder => <Input onKeyPress={this.handleKeyPress}
                                    type="text" id="new_permission" name="new_permission" placeholder={pholder}
                                    required/>
                }
              </FormattedMessage>
            </InputGroup>
            <Table responsive size="sm">
              <thead>
              <tr>
                <th>Permission</th>
                <th>Delete</th>
              </tr>
              </thead>
              <tbody>
              {table}
              </tbody>
            </Table>

          </FormGroup>
        </CardBody>
      </Card>
    );
  }
}

export default inject("appStore")(observer(PermissionForm));
