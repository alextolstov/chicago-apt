import React, {Component} from 'react';
import { Card, CardHeader, CardBody, Button, Modal, ModalBody, ModalHeader, Row, Col } from 'reactstrap';
//import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from 'mobx-react/index';
import Spinner from "../../Spinner/Spinner";
import EditOrganization from '../EditOrganization/EditOrganization';
import TreeGrid from "react-treegrid-2";
import OrganizationApi from '../../../api/OrganizationApi';
import {UiOrganizationInfo} from "../../../models/UiOrganizationInfo";
import OrganizationInfoConvertor from "../../../convertors/OrganizationInfoConvertor";

const organization_proto = require('dto/organization_pb');
const jspb = require('google-protobuf');


const selectRow = {
  mode: 'radio',
  clickToSelect: true
};

class ListOrganizations extends Component {
  constructor(props) {
    super(props);
    this.table = [];
    this.convertor = new OrganizationInfoConvertor();
    this.organizationApi = new OrganizationApi();

    this.options = {
      sortIndicator: true,
      hideSizePerPage: true,
      paginationSize: 3,
      hidePageListOnlyOnePage: true,
      clearSearch: false,
      alwaysShowAllBtns: false,
      withFirstAndLast: false,
//      onRowClick: this.onRowSelect,
    }

    this.state = {
      data: null,
      selected: {
        id: '',
        name: '',
      },
      optionsList: [],
      isLoading: true,
      openedDetails: false,
      organizationStructure: []
    }
  }

  componentDidMount() {
    this.state.isLoading = true;
    let self = this;
    this.organizationApi.getStructure(null).then(function (orgStruct) {
      let orgStructure = new UiOrganizationInfo();
      self.convertor.fromDto(orgStruct.getOrganizationInfo(), orgStructure);
      self.state.organizationStructure.push(orgStructure);
      self.setState({organizationStructure: self.state.organizationStructure, isLoading: false});
    })
  }

  onRowClick = (data) => {
    this.state.openedDetails = true;
    this.setState({selected: {id: data}});
  }

  toggleDetails = () => {
    this.setState({
      openedDetails: !this.state.openedDetails,
    });
  }

  addOrganization = (event) => {
    console.log('Add Organization');
    alert(this.props.data);
    this.setState({selected: {id: "new"}});
//    event.stopPropagation();
  }

  loadOrganizations = () => {
    console.log('List organization');
    this.componentDidMount();
  }

  render() {
    return (
      <div className="row">
        <div className="col-lg-12">
          <div className="animated">
            <Card>
              <CardHeader>
                <h3><strong><FormattedMessage id="menu.organizations.list"
                                              defaultMessage="Organizations list"/>
                </strong>
                </h3>
                <div>
                  <button onClick={this.addOrganization}
                  >
                    <strong><FormattedMessage id="menu.users.new_organization"
                                              defaultMessage="Create new organization"/>
                    </strong>
                  </button>
                </div>
              </CardHeader>
              <CardBody>
                {!this.state.isLoading &&
                <TreeGrid
                  callback={this.onRowClick}
                  data={this.state.organizationStructure}
                  options={{
                    fields: [
                      {
                        property: 'name',
                        colHeader: <FormattedMessage id="org.edit.name" defaultMessage="Organizations"/>,
                        width: '70%'
                      },
                      {
                        property: 'type',
                        colHeader: <FormattedMessage id="org.edit.type" defaultMessage="Type"/>
                      },
                      {
                        type: 'button',
                        callback: this.addOrganization,
                        colHeader: <FormattedMessage id="org.edit.action" defaultMessage="Action"/>,
                        caption: <FormattedMessage id="org.edit.addbutton" defaultMessage="Add"/>,
                        width: '5%'
                      }
                    ]
                  }}
                />
                }
                {this.state.isLoading &&
                <Spinner/>
                }
              </CardBody>
            </Card>
          </div>
        </div>
        {(this.state.selected.id) &&
        <Modal isOpen={this.state.openedDetails} toggle={this.toggleDetails} centered={true}>
          <ModalHeader toggle={this.toggleDetails}>{this.state.selected.name}</ModalHeader>
          <ModalBody>
            <EditOrganization organizationId={this.state.selected.id}
                              toggle={this.toggleDetails} loadList={this.loadList} company={this.company}/>
          </ModalBody>
        </Modal>
        }
      </div>
    );
  }
}


export default inject('appStore')(observer(ListOrganizations));
