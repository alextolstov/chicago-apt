import React, { Component } from 'react'
import { Col, Label } from 'reactstrap'
import cookies from 'react-cookies'
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table'
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css'
import { inject, observer } from 'mobx-react/index'
import 'spinkit/css/spinkit.css'
import Spinner from '../Spinner/Spinner'
import SearchApi from '../../api/SearchFiltersApi'
import UiSearchFilters from '../../models/UiSearchFilters'
import FormApi from '../../api/FormApi'

class SavedSearches extends Component {
  constructor (props) {
    super(props)

    this.state = {
      optionsList: [],
      isLoading: true,
      searchFiltersList: new Array()
    }

    this.uiSearchFilters = new UiSearchFilters()
    this.searchApi = new SearchApi()
    this.formApi = new FormApi();
    // Cookie
    this.onboarded = cookies.load('onboarded')

    this.options = {
      sortIndicator: true,
      hideSizePerPage: true,
      paginationSize: 25,
      onRowClick: this.onRowSelect,
    }

    this.selectRowProp = {
      clickToSelect: true,
      bgColor: '#f0f3f5',
      onSelect: this.onRowSelect,
    }
  }

  componentDidMount () {
    const self = this;
    this.searchApi.getSearchCatalog(this.onboarded, (e) => {
        console.log('Error load:', e)
      }
    ).then(function (options) {
      console.log(options)
      self.setState({ optionsList: options })
      self.setState({ isLoading: false })
    })
  }

  onRowSelect = (row) => {
    this.props.appStore.last_filter = row;
    this.props.history.push("/dashbord");
  }

  filterCellFormatter = (cell, row) => {
    return (
      <div className="container">
        <div className="row">
          <Col xs="10" sm="10" lg="3">
            <div>
              <p>{this.uiSearchFilters.convertPropertyType(row.type_id)} по адресу {row.city}, {row.street} {row.house}</p>
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Цена:</b> {this.formApi.stripNonNumeric(row.apt_price_from)} - {this.formApi.stripNonNumeric(row.apt_price_to)}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Площадь м²:</b> {this.formApi.stripNonNumeric(row.apt_size_from)} - {this.formApi.stripNonNumeric(row.apt_size_to)}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Кухня м²:</b> {this.formApi.stripNonNumeric(row.kitchen_size_from)} - {this.formApi.stripNonNumeric(row.kitchen_size_to)}
            </div>
          </Col>
            <Col xs="10" sm="10" lg="3">
            <div>
              <b>Балкон:</b> {row.balcony ? "Балкон/Лоджия" : "не важно"}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Потолки м:</b> {row.ceiling_height === 0 ? "" : row.ceiling_height}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Этаж:</b> {row.floor_from === 0 ? "" : row.floor_from} - {row.floor_to === 0 ? "" : row.floor_to} {row.not_first_floor === false ? "" : "не первый"} {row.not_last_floor === false ? "" : "не последний"}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Количество комнат:</b> {row.rooms_number.join()}
            </div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div>
              <b>Вид из окон:</b> {this.uiSearchFilters.convertViewFromWindow(row.windows_view)}
            </div>
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
