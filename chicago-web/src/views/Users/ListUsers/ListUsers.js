import React, {Component} from 'react';
import {Card, CardBody, CardHeader, Modal, ModalBody, ModalHeader} from 'reactstrap';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import {FormattedMessage} from 'react-intl';
import {inject, observer} from 'mobx-react/index';
import 'spinkit/css/spinkit.css';
import UserApi from '../../../api/UserApi';
import EditUser from '../EditUser';
import {toast} from 'react-toastify';
import PersonNameLocalizeApi from '../../../api/PersonNameLocalizeApi'

const user_proto = require('models/user_pb');

class ListUsers extends Component {
  constructor(props) {
    super(props);
    this.table = [];
    this.options = {
      sortIndicator: true,
      hideSizePerPage: true,
      paginationSize: 3,
      hidePageListOnlyOnePage: true,
      clearSearch: false,
      alwaysShowAllBtns: false,
      withFirstAndLast: false,
      onRowClick: this.onRowSelect
    }

    this.selectRowProp = {
      clickToSelect: true,
      bgColor: '#f0f3f5',
      onSelect: this.onRowSelect,
    };

    this.state = {
      data: null,
      userApi: new UserApi(),
      personNameLocalizer: new PersonNameLocalizeApi(),
      selected: {
        id: '',
        name: '',
      },
      optionsList: [],
      isLoading: true,
      openedDetails: false,
    };
  }

  componentDidMount() {
    const userData = this.props.appStore.userData;
    this.state.isLoading = true;
    const self = this;

    if (!userData.getOrganizationId()) {
      const current_user = sessionStorage.getItem("current_user");
      this.state.userApi.getUserById(current_user, null).then(function (userMsg) {
        const user = userMsg.getUser();
        const orgId = user.getOrganizationId();
        self.loadListUsers(orgId);
      }).catch(function (error) {
        console.log('ListUser Load User error:', error);
        toast.error(error, {
          position: toast.POSITION.TOP_LEFT
        });
      })
    } else {
      const orgId = userData.getOrganizationId();
      self.loadListUsers(orgId);
    }
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

  loadListUsers = (orgId) => {
    let userOrgs = new user_proto.UserOrganization();

    userOrgs.setOrganizationId(orgId);
    let self = this;
    this.state.userApi.getUsers(userOrgs, (e) => {
      console.log('Error load data ListUser:', e);
    }).then(function (usersMsg) {
      const usersList = usersMsg.getUsersList();
      let optionsList = [];
      for (let i = 0; i < usersList.length; i++) {
        optionsList.push({
          id: usersList[i].getUserId(),
          name: self.state.personNameLocalizer.toPersonName(usersList[i].getFirstName(), usersList[i].getMiddleName(), usersList[i].getLastName()),
          email: usersList[i].getEmail(),
        });
      }
      self.setState({optionsList, isLoading: false});
    }).catch(function (error) {
      console.log('ListUser error:', error);
      toast.error(error, {
        position: toast.POSITION.TOP_LEFT
      });
    })
  }

  addUser=() => {
    this.state.openedDetails = true;
    this.setState({selected: {id: "new"}});
  }

  loadList = () => {
    this.componentDidMount();
  }

  render() {
    return (
      <div className="row">
        <div>
          <div className="animated">
            <Card>
              <CardHeader>

                <h3><strong><FormattedMessage id="menu.users.list"
                                              defaultMessage="Employees list"/>
                </strong>
                </h3>
                <div>
                  <button onClick={this.addUser}
                  >
                    <strong><FormattedMessage id="users.edit.new_user"
                                              defaultMessage="Create new employee"/>
                    </strong>
                  </button>
                </div>
              </CardHeader>
              <CardBody>
                {!this.state.isLoading &&
                <BootstrapTable data={this.state.optionsList} version="4" hover pagination={false}
                                options={this.options} selectRow={this.selectRowProp}>
                  <TableHeaderColumn isKey dataField="name"
                                     filter={{type: 'TextFilter', placeholder: 'Поиск...', delay: 1000}}
                                     dataSort>Name</TableHeaderColumn>
                </BootstrapTable>
                }
                {this.state.isLoading &&
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
        {(this.state.selected.id) &&
        <Modal isOpen={this.state.openedDetails} toggle={this.toggleDetails} className={'modal-max'}>
          <ModalHeader toggle={this.toggleDetails}>{this.state.selected.name}</ModalHeader>
          <ModalBody>
            <EditUser userId={this.state.selected.id} loadList={this.loadList}/>
          </ModalBody>
        </Modal>
        }
      </div>
    );
  }
}

export default inject('appStore')(observer(ListUsers));
