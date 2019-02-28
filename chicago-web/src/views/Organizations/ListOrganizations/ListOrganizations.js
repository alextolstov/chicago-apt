import React, {Component} from 'react';
import {Card, CardBody, CardHeader, Col} from 'reactstrap';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from 'mobx-react/index';
import Spinner from "../../Spinner/Spinner";
import EditOrganization from '../EditOrganization/EditOrganization';
import GridTree from "react-gridtree";
//import data from './_data';
import OrganizationApi from '../../../api/OrganizationApi';
import {UiOrganizationInfo} from "../../../models/UiOrganizationInfo";
import OrganizationInfoConvertor from "../../../convertors/OrganizationInfoConvertor";

const organization_proto = require('dto/organization_pb');
const jspb = require('google-protobuf');


const selectRow = {
  mode: 'radio',
  clickToSelect: true
};

const selectRow1 = {
  mode: 'checkbox',
  clickToSelect: true,
  style: {backgroundColor: '#c8e6c9'}
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
      onRowClick: this.onRowSelect,
    }

    this.selectRowProp = {
      clickToSelect: true,
      bgColor: '#f0f3f5',
      onSelect: this.onRowSelect,
    };

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

    this.addOrganization = this.addOrganization.bind(this);
    this.loadOrganizations = this.loadOrganizations.bind(this);
  }

  componentDidMount() {
    this.state.isLoading = true;
    let self = this;
    this.organizationApi.getStructure(null).then(function (orgStruct) {
      let orgStructure = new UiOrganizationInfo();
      self.convertor.fromDto(orgStruct.getOrganizationInfo(), orgStructure);
      self.state.organizationStructure.push(orgStructure);
//      console.log(JSON.stringify(self.state.organizationStructure));
//      let organizationStructure = self.state.organizationStructure;
      self.setState({organizationStructure:self.state.organizationStructure, isLoading: false});
    })
  }

  onRowClick = (data) => {
    alert(data);
  }

  onRowSelect = (row) => {
    this.state.openedDetails = true;
    this.setState({selected: row});
  }

  toggleDetails = () => {
    this.setState({
      openedDetails: !this.state.openedDetails,
    });
  }

  addOrganization() {
    console.log('Add Organization');
    this.setState({selected: {id: "new"}});
  }

  loadOrganizations() {
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
                <GridTree
                  callback={this.onRowClick}
                  data={this.state.organizationStructure}
                  options={{
                    fields: [
                      {
                        property: 'name',
                        displayName: <FormattedMessage id="org.edit.name" defaultMessage="Organizations"/>,
                        width: '70%'
                      },
                      {
                        property: 'type',
                        displayName: <FormattedMessage id="org.edit.type" defaultMessage="Type"/>
                      }
                    ]
                  }}
                />
                }
                {/*{!this.state.isLoading &&*/}
                {/*<BootstrapTable data={this.state.optionsList} version="4" hover pagination={false}*/}
                                {/*options={this.options} selectRow={this.selectRowProp}>*/}
                  {/*<TableHeaderColumn isKey dataField="name"*/}
                                     {/*filter={{type: 'TextFilter', placeholder: 'Поиск...', delay: 1000}}*/}
                                     {/*dataSort>Организация</TableHeaderColumn>*/}
                {/*</BootstrapTable>*/}
                {/*}*/}
                {this.state.isLoading &&
                <Spinner/>
                }

              </CardBody>
            </Card>
          </div>
        </div>
        <div className="col-8">
          <EditOrganization organizationId={this.state.selected.id} loadList={this.loadList}/>
        </div>
      </div>
    );
  }
}


export default inject('appStore')(observer(ListOrganizations));
