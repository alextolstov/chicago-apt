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
    id: 'users.edit.position',
    defaultMessage: 'Введите новую позицию и нажмите Enter'
  }
});

class PositionForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      positionApi: props.positionApiParent,
      organizationId: this.props.appStore.userData.getOrganizationId(),
    };
  }

  componentDidMount = () => {
    let self = this;
    this.state.positionApi.getPositions(this.state.organizationId, null)
      .then(function (data) {
        if (data !== undefined && data !== null) {
          self.props.appStore.companyPositions = [];
          let positions = data.getPositions().getPositionsMap();

          positions.forEach((l, v) => {
            self.props.appStore.companyPositions.push({value:v, label:l});
          });
          self.props.readyPosition(true);           
        }
      });
  }

  handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      let self = this;
      let val = event.target.value;
      event.target.value = "";
      self.props.readyPosition(false);           

      this.state.positionApi.createPosition(this.state.organizationId, val, null)
        .then( (data)=> {
          this.componentDidMount();
        });
    }
  }

  render() {
    let table = this.props.appStore.companyPositions.map((position) => {
      return <tr key={position.value}>
        <td>
          <Input id={position.value} onChange={this.handleChange} bsSize="sm" className="input-sm" defaultValue={position.label}
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
          <strong><FormattedMessage id="users.edit.positions" defaultMessage="Positions"/></strong>
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

export default inject("appStore")(observer(PositionForm));
