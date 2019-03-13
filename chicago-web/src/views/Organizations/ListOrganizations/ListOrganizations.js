import React, {Component} from 'react';
import { Card, CardHeader, CardBody, Button, Modal, ModalBody, ModalHeader, Row, Col } from 'reactstrap';
//import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from 'mobx-react/index';
import Spinner from "../../Spinner/Spinner";
import EditOrganization from '../EditOrganization/EditOrganization';
import TreeGrid from "react-treegrid-2";
import OrganizationApi from '../../../api/OrganizationApi';
import UiOrganizationInfo from "../../../models/UiOrganizationInfo";
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
    this.wordsSeparator = " ";

    this.state = {
      data: null,
      isNew: false,
      selectedOrg: null,
      isLoading: true,
      openedDetails: false,
      organizationStructure: [],
      dialogHeaderAction: "",
      dialogHeaderSubject: ""
    }
  }

  componentDidMount() {
    window.MyVars = this;
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
    this.state.dialogHeaderAction = <FormattedMessage id="org.edit" defaultMessage="Edit"/>;
    this.state.dialogHeaderSubject = data.name;
    this.state.openedDetails = true;
    this.setState({selectedOrg: data, isNew: false});
  }

  addOrganization = (data) => {
    this.state.dialogHeaderAction = <FormattedMessage id="org.list.add" defaultMessage="Add"/>;
    switch (data.type) {
      case 0:
        this.state.dialogHeaderSubject = <FormattedMessage id="org.company" defaultMessage="Company"/>;
        break;
      case 1:
        this.state.dialogHeaderSubject = <FormattedMessage id="org.branch" defaultMessage="Branch"/>;
        break;
    }
    this.state.openedDetails = true;
    this.setState({selectedOrg: data, isNew: true});
  }

  toggleDetails = (data) => {
    this.setState({openedDetails: !this.state.openedDetails});

    if (data.currentTarget === undefined) {
      let self = this;
      this.organizationApi.getStructure(null).then((orgStruct) => {
        let orgStructure = new UiOrganizationInfo();
        self.convertor.fromDto(orgStruct.getOrganizationInfo(), orgStructure);
        self.state.organizationStructure = [];
        self.state.organizationStructure.push(orgStructure);
        self.setState({organizationStructure: self.state.organizationStructure, isLoading: false});
      })
    }
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
                        colHeader: <FormattedMessage id="org.list.name" defaultMessage="Organizations"/>,
                        width: '70%'
                      },
                      {
                        property: 'type',
                        colHeader: <FormattedMessage id="org.list.type" defaultMessage="Type"/>
                      },
                      {
                        type: 'button',
                        callback: this.addOrganization,
                        colHeader: <FormattedMessage id="org.list.action" defaultMessage="Action"/>,
                        caption: <FormattedMessage id="org.list.add" defaultMessage="Add"/>,
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
        {(this.state.selectedOrg) &&
        <Modal isOpen={this.state.openedDetails} toggle={this.toggleDetails} centered={true}>
          <ModalHeader toggle={this.toggleDetails}>
            {this.state.dialogHeaderAction}
            {this.wordsSeparator}
            {this.state.dialogHeaderSubject}
          </ModalHeader>
          <ModalBody>
            <EditOrganization organization={this.state.selectedOrg} isNew={this.state.isNew}
                              toggle={this.toggleDetails} />
          </ModalBody>
        </Modal>
        }
      </div>
    );
  }
}


export default inject('appStore')(observer(ListOrganizations));
