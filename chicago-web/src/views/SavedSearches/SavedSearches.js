import React, { Component } from 'react'
import { Col, Label } from 'reactstrap'
import cookies from 'react-cookies'
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table'
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css'
import { inject, observer } from 'mobx-react/index'
import 'spinkit/css/spinkit.css'
import Spinner from '../Spinner/Spinner'
import SearchApi from '../../api/SearchFiltersApi'
import UiSearchFilters, { PropertyType } from '../../models/UiSearchFilters'

const user_proto = require('dto/user_pb')

class SavedSearches extends Component {
  constructor (props) {
    super(props)

    this.state = {
      data: null,
      selected: {
        id: '',
        name: '',
      },
      optionsList: [],
      isLoading: true,
      openedDetails: false,
      searchFiltersList: new Array()
    }

    this.uiSearchFilters = new UiSearchFilters()
    this.searchApi = new SearchApi()
    // Cookie
    this.onboarded = cookies.load('onboarded')

    this.table = []
    this.options = {
      sortIndicator: true,
      hideSizePerPage: true,
      paginationSize: 3,
      onRowClick: this.onRowSelect,
    }

    this.selectRowProp = {
      clickToSelect: true,
      bgColor: '#f0f3f5',
      onSelect: this.onRowSelect,
    }

  }

  componentDidMount () {
    const userData = this.props.appStore.userData
    let userId = new user_proto.UserId()
    userId.setUserId(this.onboarded)

    const self = this
    this.searchApi.getSearchCatalog(userId, (e) => {
        console.log('Error load:', e)
      }
    ).then(function (options) {
      console.log(options)
      self.setState({ optionsList: options })
      self.setState({ isLoading: false })
    })
  }

  // if (!userData.organization_id) {
  //   const current_user = sessionStorage.getItem("current_user");
  //   this.state.userApi.getUserById(current_user, null).then(function (user) {
  //     self.loadListUsers(user.organization_id);
  //   }).catch(function (error) {
  //     console.log('ListUser Load User error:', error);
  //     toast.error(error, {
  //       position: toast.POSITION.TOP_LEFT
  //     });
  //   })
  // } else {
  //   const orgId = userData.organization_id;
  //   self.loadListUsers(orgId);
  // }

  onRowSelect = (row) => {
    this.state.openedDetails = true
    this.setState({ selected: row })
  }

  toggleDetails = () => {
    this.setState({
      openedDetails: !this.state.openedDetails,
    })
  }

  getListAsString = (list) => {
    if (list === undefined || list === null) {
      return 'N/A'
    }
    return list.join();
  }

  stripNonNumeric = (strValue) => {
    if (strValue === undefined || strValue === null) {
      return "N/A"
    }

    var validChars = /[0-9]/
    var strIn = strValue
    var strOut = ''
    for (let i = 0; i < strIn.length; i++) {
      strOut += (validChars.test(strIn.charAt(i))) ? strIn.charAt(i) : ''
    }
    return strOut.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }


  convertPropertyType = (type_id) => {
    switch (type_id) {
      case PropertyType.APARTMENT:
        return 'Квартира'
      case PropertyType.ROOM:
        return 'Комната'
      case PropertyType.HOUSE:
        return 'Дом'
    }
  }
  filterCellFormatter = (cell, row) => {
    return (
      <div className="container">
        <div className="row">
          <Col xs="10" sm="10" lg="3">
            <div><Label>{this.convertPropertyType(row.type_id)} по
              адресу {row.city}, {row.street} {row.house}</Label>{this.stripNonNumeric(row.apt_price)}</div>
          </Col>

        </div>
      </div>
    )
  }

  render () {
    return (
      <div className="row">
        <div>
          <div className="animated">
            {
              !this.state.isLoading &&
              <BootstrapTable data={this.state.optionsList} version="4" hover pagination={false}
                              options={this.options} selectRow={this.selectRowProp}>
                <TableHeaderColumn dataField="description" isKey width='10%'
                                   dataFormat={this.filterCellFormatter}>Сохраненные поиски</TableHeaderColumn>

              </BootstrapTable>
            }
            {
              this.state.isLoading &&
              <Spinner/>
            }
          </div>
        </div>
      </div>
    )
  }
}

export default inject('appStore')(observer(SavedSearches))
