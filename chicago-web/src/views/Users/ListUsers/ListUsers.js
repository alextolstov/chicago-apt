import React, {Component} from 'react';
import {Card, CardHeader, CardBody} from 'reactstrap';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist//react-bootstrap-table-all.min.css';
import {inject, observer} from "mobx-react/index";

import data from './_data';
import UserApi from '../../../api/UserApi';


class ListUsers extends Component {
  constructor(props) {
    super(props);

    this.table=data.rows;
    this.userApi=new UserApi();
 
    this.options={
      sortIndicator: true,
      hideSizePerPage: true,
      paginationSize: 3,
      hidePageListOnlyOnePage: true,
      clearSearch: true,
      alwaysShowAllBtns: false,
      withFirstAndLast: false
    }

    this.state={
      data: null,
    }
  }

  componentDidMount() {
    console.log('ListUsers:componentDidMount appStore=', this.props.appStore);
    const userData=this.props.appStore.userData;
    console.log('ListUsers:componentDidMount userData=', userData);
    const organizationId=userData.getOrganizationId();
    console.log('ListUsers:componentDidMount organizationId=', organizationId);
 /*   
    this.UserApi.getUserById(this.state.userId, this.handleError).
      then(function (userMsg) {
      if (userMsg != null) {
        self.state.user = userMsg.getUser();
  */
  }

  render() {

    return (
      <div className="animated">
        <Card>
          <CardHeader>
            <i className="icon-menu"></i>Data Table{' '}
            <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            <div className="card-header-actions">
              <a href="https://github.com/AllenFang/react-bootstrap-table" rel="noopener noreferrer" target="_blank" className="card-header-action">
                <small className="text-muted">docs</small>
              </a>
            </div>
          </CardHeader>
          <CardBody>
            <BootstrapTable data={this.table} version="4" striped hover pagination search options={this.options}>
              <TableHeaderColumn dataField="name" dataSort>Name</TableHeaderColumn>
              <TableHeaderColumn isKey dataField="email">Email</TableHeaderColumn>
              <TableHeaderColumn dataField="age" dataSort>Age</TableHeaderColumn>
              <TableHeaderColumn dataField="city" dataSort>City</TableHeaderColumn>
            </BootstrapTable>
          </CardBody>
        </Card>
      </div>
    );
  }
}


export default inject("appStore")(observer(ListUsers));