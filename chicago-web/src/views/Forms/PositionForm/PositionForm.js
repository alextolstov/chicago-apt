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

    this.state = {
      positionApiParent: props.positionApiParent,
      organizationId: props.organizationId,
      positions: new Map()
    };
  }

  componentDidMount = () => {
    let self = this;
    this.state.positionApiParent.getPositions(this.state.organizationId, null)
      .then(function (data) {
        if (data !== undefined && data !== null) {
          let pos = data.getPositions().getPositionsMap();
          self.setState({positions: pos})
        }
      });
  }

  handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      let self = this;
      let val = event.target.value;
      event.target.value = "";

      this.state.positionApiParent.createPosition(this.state.organizationId, val, null)
        .then(function (data) {
          if (data !== undefined && data !== null) {
            let newPos = data.getPosition();
            self.state.positions.set(newPos.getPositionId(), newPos.getDescription());
            self.setState({positions: self.state.positions});
          }
        });
    }
  }

  render() {
    let arr = [];
    this.state.positions.forEach((l, r) => {
      arr.push([l, r]);
    });
    let table = arr.map((r) => {
      return <tr key={r[1]}>
        <td>
          <Input id={r[1]} onChange={this.handleChange} bsSize="sm" className="input-sm" defaultValue={r[0]}
                 type="text"/>
        </td>
        <td>
          <i className="cui-delete icons font-2xl"></i>
        </td>
      </tr>
    });

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
                  pholder => <Input onKeyPress={this.handleKeyPress}
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
              {table}
              </tbody>
            </Table>

          </FormGroup>
        </CardBody>
      </Card>
    );
  }
}

export default PositionForm;
