import React, {Component} from 'react';
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
    id: 'users.edit.newposition',
    defaultMessage: 'New position'
  }
});

class PositionForm extends Component {
  constructor(props) {
    super(props);
  }

  handleChange = (event) => {
  }

  render() {
    return (
      <Card id='add_position_card' hidden>
        <CardHeader>
          <button onClick={this.saveNewPosition}>
            <i className="icon-cloud-upload"></i>
          </button>
          <strong><FormattedMessage id="users.edit.newposition" defaultMessage="New position"/></strong>
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
                  pholder => <Input onChange={this.handleChange}
                                    type="text" id="new_position" name="new_position" placeholder={pholder}
                                    required/>
                }
              </FormattedMessage>
            </InputGroup>
            <Table responsive size="sm">
              <thead>
              <tr>
                <th>Position</th>
                <th>Delete</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td>
                  <Input onChange={this.handleChange} bsSize="sm" className="input-sm" value="Barber" type="text"/>
                </td>
                <td>
                  <i className="cui-delete icons font-2xl"></i>
                </td>
              </tr>
              <tr>
                <td>
                  <Input onChange={this.handleChange} bsSize="sm" className="input-sm" value="Manager" type="text"/>
                </td>
                <td>
                  <i className="cui-delete icons font-2xl"></i>
                </td>
              </tr>
              <tr>
                <td>
                  <Input onChange={this.handleChange} bsSize="sm" className="input-sm" value="Security" type="text"/>
                </td>
                <td>
                  <i className="cui-delete icons font-2xl"></i>
                </td>
              </tr>
              <tr>
                <td>
                  <Input onChange={this.handleChange} bsSize="sm" className="input-sm" value="Help Desk" type="text"/>
                </td>
                <td>
                  <i className="cui-delete icons font-2xl"></i>
                </td>
              </tr>
              </tbody>
            </Table>

          </FormGroup>
        </CardBody>
      </Card>
    );
  }
}

export default PositionForm;
