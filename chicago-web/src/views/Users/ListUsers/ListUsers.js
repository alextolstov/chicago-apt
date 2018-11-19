import React, {Component} from 'react';
import {Card, CardHeader, CardBody} from 'reactstrap';
import BootstrapTable  from 'react-bootstrap-table-next';

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
        this.rowEvents = {
          onClick: (e, row, rowIndex) => {
              console.log('clicked on row:', row);
              console.log(`clicked on row  with index: ${rowIndex}`);
              this.setState({selected: row.name});
          },
          onMouseEnter: (e, row, rowIndex) => {
            // console.log(`enter on row with index: ${rowIndex}`);
          }
        };

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
            selected: null,
            optionsList: [],
              columns: [{
                dataField: 'name',
                text: 'Name'
              },{
                dataField: 'email',
                text: 'Email',
                sort: true
              }],
        };
    }
    componentDidMount() {
        console.log('usersList:componentDidMount appStore=', this.props.appStore);
        const userData=this.props.appStore.userData;
        console.log('usersList:componentDidMount userData=', userData);
        const organizationId=userData.getOrganizationId();
        console.log('usersList:componentDidMount organizationId=', organizationId);

        let userOrgs=new user_proto.UserOrganization();
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
                console.log(usersList[i]);
                console.log(usersList[i].getFirstName(), usersList[i].getMiddleName(), usersList[i].getLastName());
                optionsList.push({
                    id:usersList[i].getUserId(),
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
            <div className="row">
            <div className="col-4">
                <div className="animated">
                <Card>
                    <CardHeader>
                        <i className="icon-menu"></i>React-bootstrap-table2{' '}
                        <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
                        <div className="card-header-actions">
                            <a href="https://github.com/AllenFang/react-bootstrap-table" rel="noopener noreferrer" target="_blank" className="card-header-action">
                                <small className="text-muted">docs</small>
                            </a>
                        </div>
                    </CardHeader>
                    <CardBody>
                        <BootstrapTable
                            striped
                            hover
                            keyField='email' 
                            data={ this.state.optionsList } 
                            columns={this.state.columns}
                            rowEvents={ this.rowEvents }
                        />            
                    </CardBody>
                </Card>
                </div>
            </div>
            <div className="col-8">
                    {this.state.selected}
            </div>
            </div>
        );
    }
}


export default inject('appStore')(observer(ListUsers));
