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
    id: 'users.edit.newposition',
    defaultMessage: 'New position'
  }
});

class PositionForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      positionApi: props.positionApiParent,
      organizationId: this.props.appStore.userData.getOrganizationId(),
      positionsArr: [],
      positionsMap: new Map()
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
            self.state.positionsArr.push([v, l]);
          });
          self.props.readyPosition();            // setState  and render parent
//          self.setState({positionsArr: self.state.positionsArr});
        }
      });
  }

  handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      let self = this;
      let val = event.target.value;
      event.target.value = "";

      this.state.positionApi.createPosition(this.state.organizationId, val, null)
        .then(function (data) {
          if (data !== undefined && data !== null) {
            let newPos = data.getPosition();
            self.props.appStore.companyPositions.push({value:newPos.getPositionId(), label:newPos.getDescription()});
            self.state.positionsArr.push([newPos.getPositionId(), newPos.getDescription()]);
            self.setState({positionsArr: self.state.positionsArr});
          }
        });
    }
  }

  render() {
    let table = this.state.positionsArr.map((r) => {
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
      <Card id='add_position_card' hidden>
        <CardHeader>
          <button onClick={this.saveNewPosition}>
            <i className="icon-cloud-upload"></i>
          </button>
          <strong><FormattedMessage id="users.edit.newposition" defaultMessage="Type new position and press Enter"/></strong>
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
