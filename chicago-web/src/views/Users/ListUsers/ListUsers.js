import React, {Component} from 'react';
import {Card, CardHeader, CardBody} from 'reactstrap';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist//react-bootstrap-table-all.min.css';
import {inject, observer} from 'mobx-react/index';

import data from './_data';
import UserApi from '../../../api/UserApi';
const jspb = require('google-protobuf');
const user_proto = require('models/user_pb');

class ListUsers extends Component {
    constructor(props) {
        super(props);

        this.table=data.rows;
  //      this.userApi=new UserApi();
 
        this.options={
            sortIndicator: true,
            hideSizePerPage: true,
            paginationSize: 3,
            hidePageListOnlyOnePage: true,
            clearSearch: true,
            alwaysShowAllBtns: false,
            withFirstAndLast: false
        };

        this.state={
            data: null,
            userApi: new UserApi(),
            optionsList: [],
        };
    }

    componentDidMount() {
        console.log('usersList:componentDidMount appStore=', this.props.appStore);
        const userData=this.props.appStore.userData;
        console.log('usersList:componentDidMount userData=', userData);
        const organizationId=userData.getOrganizationId();
        console.log('usersList:componentDidMount organizationId=', organizationId);
 //       const user = jspb.Message.cloneMessage(this.props.appStore.userData);
 
        let userOrgs = new user_proto.UserOrganization();
        const orgId=userData.getOrganizationId();
        userOrgs.setOrganizationId(orgId);
        let self = this;
        this.state.userApi.getUsers(userOrgs, (e) => {
            console.log('Error load data ListUser:', e);
        }).
        then(function (usersMsg) {
            const usersList=usersMsg.getUsersList();
            console.log('usersList=', usersList);
            let optionsList=[];
            for(let i=0;i<usersList.length;i++) {
                console.log(usersList[i].getFirstName(), usersList[i].getMiddleName(), usersList[i].getLastName());
                optionsList.push({
                    name: usersList[i].getFirstName()+' '+usersList[i].getMiddleName()
                        +' '+usersList[i].getLastName(),
                    email: usersList[i].getEmail(),
                });
              
            }
            self.setState({optionsList});
                
        }).
        catch( function (error) {
          console.log('ListUser error:', error);
        })   
    }

    render() {

        return (
            <div class="row">
            <div class="col">
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
                        <BootstrapTable data={this.state.optionsList} version="4" striped hover pagination search options={this.options}>
                            <TableHeaderColumn dataField="name" dataSort>Name</TableHeaderColumn>
                            <TableHeaderColumn isKey dataField="email">Email</TableHeaderColumn>
                        </BootstrapTable>
                    </CardBody>
                </Card>
                </div>
            </div>
            <div class="col">
              1/2
            </div>
            </div>
        );
    }
}


export default inject('appStore')(observer(ListUsers));
