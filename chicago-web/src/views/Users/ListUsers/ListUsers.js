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
import Spinner from "../../Spinner/Spinner";

const user_proto = require('dto/user_pb');

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

    if (!userData.organization_id) {
      const current_user = sessionStorage.getItem("current_user");
      this.state.userApi.getUserById(current_user, null).then(function (user) {
        self.loadListUsers(user.organization_id);
      }).catch(function (error) {
        console.log('ListUser Load User error:', error);
        toast.error(error, {
          position: toast.POSITION.TOP_LEFT
        });
      })
    } else {
      const orgId = userData.organization_id;
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
    }).then(function (usersList) {
      let optionsList = [];
      for (let i = 0; i < usersList.length; i++) {
        const mapPositions = usersList[i].positions;
        let strPositions = "";
        for (let j = 0; j < mapPositions.length; j++) {
          if (j > 0) strPositions += ",";
          strPositions += mapPositions[j][1];
        }

        optionsList.push({
          id: usersList[i].user_id,
          name: self.state.personNameLocalizer.toPersonName(usersList[i].first_name, usersList[i].middle_name, usersList[i].last_name),
          positions: strPositions,
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

  addUser = () => {
    this.props.history.push("/dashbord")
    // this.state.openedDetails = true;
    // this.setState({selected: {id: "new"}});
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
                                     dataSort>Сотрудник</TableHeaderColumn>
                  <TableHeaderColumn dataField="positions">Позиция</TableHeaderColumn>
                </BootstrapTable>
                }
                {this.state.isLoading &&
                <Spinner/>
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
