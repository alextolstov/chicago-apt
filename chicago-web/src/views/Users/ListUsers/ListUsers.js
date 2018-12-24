import React, {Component} from 'react';
import {Card, CardHeader, CardBody, Button} from 'reactstrap';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist//react-bootstrap-table-all.min.css';

import {defineMessages, FormattedMessage} from 'react-intl';

import {inject, observer} from 'mobx-react/index';
import 'spinkit/css/spinkit.css';


import UserApi from '../../../api/UserApi';

import EditUser from '../EditUser';
import {ToastContainer, toast} from 'react-toastify';
import {language} from '../../../index';

const jspb = require('google-protobuf');
const user_proto = require('models/user_pb');

class ListUsers extends Component {
    constructor(props) {
        super(props);
        this.onRowSelect = (row, isSelected, e) => {
            this.setState({selected: row});
        }
        this.table = [];
        this.options = {
          sortIndicator: true,
          hideSizePerPage: true,
          paginationSize: 3,
          hidePageListOnlyOnePage: true,
          clearSearch: false,
          alwaysShowAllBtns: false,
          withFirstAndLast: false
        }

        this.state={
            data: null,
            userApi: new UserApi(),
            selected: { id : '',
                        name: '',
                      },
            optionsList: [],
            isLoading : true,
        };
        this.selectRowProp = {
            mode: 'radio',
            clickToSelect: true,
            bgColor:  '#f0f3f5',
            onSelect: this.onRowSelect,
        };
        this.addUser=this.addUser.bind(this);
        this.loadList=this.loadList.bind(this);
    }
    componentDidMount() {
        const userData=this.props.appStore.userData;
        const organizationId=userData.getOrganizationId();
        this.state.isLoading=true;

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
                    name: (language=='ru')? usersList[i].getLastName()+' '+usersList[i].getFirstName()+' '+usersList[i].getMiddleName():
                                            usersList[i].getFirstName()+' '+usersList[i].getLastName(),
                    email: usersList[i].getEmail(),
                });
              
            }
            self.setState({optionsList, isLoading: false});
                
        }).
        catch(function (error) {
          console.log('ListUser error:', error);
          toast.error(error, {
            position: toast.POSITION.TOP_LEFT
          });
        })   
    }

    addUser() {
        this.setState({selected : {id: "new"}});
    }

    loadList() {
        this.componentDidMount();
    }

    render() {
        return (
            <div className="row">
            <div className="col-4">
                <div className="animated">
                <Card>
                  <CardHeader>
           
                     <h3><strong><FormattedMessage id="menu.users.listusers"
                                    defaultMessage="List user" />
                     </strong>
                     </h3>
                    <div>           
                    <button  onClick={this.addUser}  
                    >
                    <strong><FormattedMessage id="users.edit.new_user"
                                              defaultMessage="Create new user"/>
                    </strong>
                    </button>
                    </div>
                    </CardHeader>
                     <CardBody>
                                {!this.state.isLoading&&
                                    <BootstrapTable data={this.state.optionsList} version="4" hover pagination={false}
                                        options={this.options} selectRow={this.selectRowProp}>
                                        <TableHeaderColumn isKey dataField="name" filter={{type: 'TextFilter', placeholder: 'Поиск...', delay: 1000}} dataSort>Name</TableHeaderColumn>
                                    </BootstrapTable>
                                }
                                {this.state.isLoading&&
                                    <div className="sk-circle">
                                    <div className="sk-circle1 sk-child"></div>
                                    <div className="sk-circle2 sk-child"></div>
                                    <div className="sk-circle3 sk-child"></div>
                                    <div className="sk-circle4 sk-child"></div>
                                    <div className="sk-circle5 sk-child"></div>
                                    <div className="sk-circle6 sk-child"></div>
                                    <div className="sk-circle7 sk-child"></div>
                                    <div className="sk-circle8 sk-child"></div>
                                    <div className="sk-circle9 sk-child"></div>
                                    <div className="sk-circle10 sk-child"></div>
                                    <div className="sk-circle11 sk-child"></div>
                                    <div className="sk-circle12 sk-child"></div>
                                    </div>
                                }
                                
                     </CardBody>
                </Card>
                </div>
            </div>
                <div className="col-8">
                    { (this.state.selected.id)  &&
                        <EditUser userId={this.state.selected.id} loadList={this.loadList} />
                    }    
            </div>
            </div>
        );
    }
}


export default inject('appStore')(observer(ListUsers));
