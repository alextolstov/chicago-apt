import React, {Component} from 'react';
import {Card, CardHeader, CardBody, Button} from 'reactstrap';
import BootstrapTable  from 'react-bootstrap-table-next';
import {defineMessages, FormattedMessage} from 'react-intl';

import {inject, observer} from 'mobx-react/index';


import data from './_data';
import UserApi from '../../../api/UserApi';

import EditUser from '../EditUser';


const jspb = require('google-protobuf');
const user_proto = require('models/user_pb');



class ListUsers extends Component {
    constructor(props) {
        super(props);

        this.table=data.rows;
        this.rowEvents = {
          onClick: (e, row, rowIndex) => {
              console.log('clicked on row:', row);
              console.log(`clicked on row  with index: ${rowIndex}`);
              this.setState({selected: row});
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
            selected: { id : 'current',
                        name: '',
                      },
            optionsList: [],
              columns: [{
                dataField: 'name',
                text: 'Name'
              },/*{
                dataField: 'email',
                text: 'Email',
                sort: true
              }*/],
        };
        this.addUser=this.addUser.bind(this);
        this.loadList=this.loadList.bind(this);
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
            let optionsList=[];
            for(let i=0;i<usersList.length;i++) {
                optionsList.push({
                    id:usersList[i].getUserId(),
                    name: usersList[i].getLastName() +' '+  usersList[i].getFirstName()  +' ' + usersList[i].getMiddleName()
                        ,
                    email: usersList[i].getEmail(),
                });
              
            }
            self.setState({optionsList});
                
        }).
        catch( function (error) {
          console.log('ListUser error:', error);
        })   
    }

    addUser() {
        console.log('Add User');
        this.setState({selected : {id: "new"}});
    }

    loadList() {
        console.log('ListUsers loadList');
        this.componentDidMount();
    }

    render() {
        return (
            <div className="row">
            <div className="col-4">
                <div className="animated">
                <Card>
                    <CardHeader>
                     <strong><FormattedMessage id="menu.users.listusers"
                            defaultMessage="List user" />
                     </strong>
                    <div>           
                    <button  onClick={this.addUser}  
                    >
                    <strong><FormattedMessage id="users.edit.new_user"
                            defaultMessage="Create new user" />
                    </strong>
                    </button>
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
                    <EditUser userId={this.state.selected.id} loadList={this.loadList}  />
            </div>
            </div>
        );
    }
}


export default inject('appStore')(observer(ListUsers));
