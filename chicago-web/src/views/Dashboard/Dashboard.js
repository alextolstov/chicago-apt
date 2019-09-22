import React, { Component } from 'react'
import cookies from 'react-cookies';
import {
  Button,
  ButtonDropdown,
  ButtonGroup,
  ButtonToolbar,
  Card,
  CardBody,
  CardHeader,
  Col,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  FormText,
  Input,
  InputGroupAddon,
  Label,
  Row,
  UncontrolledTooltip,
} from 'reactstrap'
import { inject, observer } from 'mobx-react'
import UiSearchFilters, { FloorBeamsMaterial, Market, PropertyType, ViewFromWindow } from '../../models/UiSearchFilters'
import UiProperty from '../../models/UiProperty'
import Select from 'react-select'
import CityApi from '../../api/CityApi'
import SearchApi from '../../api/SearchFiltersApi'
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table'
import data from './_test_data'

class Dashboard extends Component {
  constructor (props) {
    super(props)

    this.state = {
      type_name: 'Квартира',
      market_name: 'Любой',
      rooms: 'Любое',
      dropdownOpen: false,
      windowRadioSelected: 1,
      ceilingRadioSelected: 1,
      notFirstFloorRadioSelected: false,
      lastOrNotLastFloorsRadioSelected: false,
      balconRadioSelected: false,
      showFilters: false,

      selectedDistricts: [],
      districtOptions: [],

      selectedSubwayStations: [],
      subwayStationOptions: [],

      // Cookie
      onboarded: cookies.load("onboarded"),
      propertiesList: new Array()
    }

    this.uiSearchFilters = new UiSearchFilters()
    this.cityApi = new CityApi()
    this.searchApi = new SearchApi()
    this.table = data.rows

    let testProperty = new UiProperty()
    testProperty.property_id = 1
    testProperty.city = 'Санкт-Петербург'
    testProperty.street = 'Съезжинская ул.'
    testProperty.house = '19'
    testProperty.floor_plan = null
    testProperty.type_id = PropertyType.APARTMENT
    testProperty.market_id = Market.SECOND
    testProperty.apt_price = 100000
    testProperty.apt_size = 120
    testProperty.windows_view = ViewFromWindow.STREET_VIEW
    testProperty.balcony = true
    testProperty.kitchen_size = 12
    testProperty.ceiling_height = 3.27
    testProperty.floor = 4
    testProperty.floors_in_house = 4
    testProperty.rooms_number = 4
    testProperty.floor_beams_material = FloorBeamsMaterial.METAL_CONCRETE
    testProperty.floor_depreciation_percent = 41
    testProperty.sewer_depreciation_percent = 23
    testProperty.walls_depreciation_percent = 45
    testProperty.complains_number = 21

    //this.state.propertiesList.push(testProperty)
  }

  componentDidMount () {
    // If cookie not found just create it
    if (!this.state.onboarded) {
      let newCookie = this.uuidv4();
      cookies.save("onboarded", newCookie, {path: "/"});
      this.setState({onboarded: newCookie});
    }

    let self = this
    const $ = window.$
    $('#geoaddress').suggestions({
      token: '6c595d7dcead327d7c7b5a1d74d37ba7291428c6',
      type: 'ADDRESS',
      /* Вызывается, когда пользователь выбирает одну из подсказок */
      onSelect: function (suggestion) {
        self.uiSearchFilters.city_id = suggestion.data.city_fias_id
        console.log(suggestion)
      }
    })

    $('#geocity').suggestions({
      token: '6c595d7dcead327d7c7b5a1d74d37ba7291428c6',
      type: 'ADDRESS',
      hint: false,
      bounds: 'city',
      constraints: {
        label: '',
        locations: { city_type_full: 'город' }
      },      /* Вызывается, когда пользователь выбирает одну из подсказок */
      onSelect: function (suggestion) {
        self.uiSearchFilters.city_id = suggestion.data.city_fias_id
        console.log(suggestion)
        // Call server
        self.cityApi.getCity(self.uiSearchFilters.city_id, (e) => {
            console.log('Error load:', e)
          }
        ).then(function (obj) {
          // Districts
          let districtOptMap = new Map()
          for (let d of obj.districts) {
            let valueArr = districtOptMap.get(d.district_classifier)
            if (valueArr === undefined) {
              valueArr = new Array()
            }
            valueArr.push({ district_id: d.district_id, district_name: d.district_name })
            districtOptMap.set(d.district_classifier, valueArr)
          }

          let districtOpt = new Array()
          districtOptMap.forEach(function (val, key) {
            let classifier = { label: key, options: [] }
            for (let v of val) {
              classifier.options.push({
                value: v.district_id,
                label: v.district_name
              })
            }
            districtOpt.push(classifier)
          })
          self.setState({ districtOptions: districtOpt })

          // Subway Stations
          let subwayStationOpt = new Array()
          for (let sl of obj.subway_lines) {
            let line = { label: sl.line_name, options: [] }
            for (let ss of sl.subway_stations) {
              line.options.push({
                value: ss.station_id,
                label: ss.station_name
              })
            }
            subwayStationOpt.push(line)
          }
          self.setState({ subwayStationOptions: subwayStationOpt })
        })
      }
    })
  }

  uuidv4 = () => {
    return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
      (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
  }

  toggle = () => {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen,
    })
  }

  onWindowRadioBtnClick = (radioSelected) => {
    this.uiSearchFilters.windows_view = radioSelected
    this.setState({ windowRadioSelected: radioSelected })
  }

  onCeilingRadioBtnClick = (radioSelected) => {
    this.uiSearchFilters.ceiling_height = radioSelected
    this.setState({ ceilingRadioSelected: radioSelected })
  }

  onNotFirstFloorsRadioBtnClick = (radioSelected) => {
    this.uiSearchFilters.not_first_floor = !this.state.notFirstFloorRadioSelected
    this.setState({ notFirstFloorRadioSelected: !this.state.notFirstFloorRadioSelected })
  }

  onLastOrNotLastFloorsRadioBtnClick = (radioSelected) => {
    if (radioSelected === 2) {
      this.uiSearchFilters.not_last_floor = true
      this.uiSearchFilters.last_floor = false
    } else {
      this.uiSearchFilters.last_floor = true
      this.uiSearchFilters.not_last_floor = false
    }
    this.setState({ lastOrNotLastFloorsRadioSelected: radioSelected })
  }

  onBalconRadioBtnClick = (radioSelected) => {
    this.uiSearchFilters.balcony = !this.state.balconRadioSelected
    this.setState({ balconRadioSelected: !this.state.balconRadioSelected })
  }

  handleClick = (e) => {
    e.stopPropagation()
  }

  handleTypeClick = (e) => {
    this.uiSearchFilters.type_id = e.target.attributes.id
    this.setState({ type_name: e.nativeEvent.target.innerText })
  }

  handleMarketClick = (e) => {
    this.uiSearchFilters.market_id = e.target.attributes.id
    this.setState({ market_name: e.nativeEvent.target.innerText })
  }

  handleRoomsClick = (e) => {
    let clicked_rooms = parseInt(e.target.attributes.id.value, 10)
    if (this.uiSearchFilters.rooms_number.has(clicked_rooms)) {
      this.uiSearchFilters.rooms_number.delete(clicked_rooms)
    } else {
      this.uiSearchFilters.rooms_number.add(clicked_rooms)
    }

    var show_rooms = [...this.uiSearchFilters.rooms_number].join(',')
    if (show_rooms === '') {
      show_rooms = 'Любое'
    }
    this.setState({ rooms: show_rooms })
  }

  handlePriceFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.apt_price_from = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handlePriceToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.apt_price_to = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handleAptSizeFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.apt_size_from = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handleAptSizeToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.apt_size_to = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handleKitchenSizeFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.kitchen_size_from = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handleKitchenSizeToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value)
    this.uiSearchFilters.kitchen_size_to = parseInt(e.target.value.replace(/\D/g, ''))
  }

  handleFilterButtonClick = (e) => {
    this.setState({ showFilters: !this.state.showFilters })
  }

  handleSearchButtonClick = (e) => {
    let self = this;
    this.uiSearchFilters.user_id = this.state.onboarded;
    console.log(this.uiSearchFilters)
    this.searchApi.search(this.uiSearchFilters, (e) => {
      console.log('Error load:', e)
    })
      .then(function (properties) {
        self.setState({propertiesList : properties});
        // for(let p of propertiesList) {
        //   this.statepropertiesList.push(p);
        // }
      })
  }

  saveDistrictsChange = (selectedDistricts) => {
    for (let d of selectedDistricts) {
      let distictId = parseInt(d.value, 10);
//      if (!this.uiSearchFilters.district_id.has(distictId)) {
        this.uiSearchFilters.district_id.add(distictId);
//      }
    }
    this.setState({ selectedDistricts })
  }

  saveSubwayStationsChange = (selectedSubwayStations) => {
    for (let s of selectedSubwayStations) {
      let stationId = parseInt(s.value, 10);
//      if (!this.uiSearchFilters.subway_station_id.has(stationId)) {
        this.uiSearchFilters.subway_station_id.add(stationId);
  //    }
    }
    this.setState({ selectedSubwayStations })
  }

  stripNonNumeric = (strValue) => {
    var validChars = /[0-9]/
    var strIn = strValue
    var strOut = ''
    for (let i = 0; i < strIn.length; i++) {
      strOut += (validChars.test(strIn.charAt(i))) ? strIn.charAt(i) : ''
    }
    return strOut.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }

  imageCellFormatter = (cell, row) => {
    return (
      <div>
        <div>{row.image}</div>
      </div>
    )
  }

  convertViewFromWindow = (windows_view) => {
    switch (windows_view) {
      case ViewFromWindow.VIEW_NOT_IMPORTANT:
        return "Не важно";
      case ViewFromWindow.STREET_VIEW:
        return "На улицу";
      case ViewFromWindow.BACKYARD_VIEW:
        return "Во двор";
    }
  }

  convertPropertyType = (type_id) => {
    switch (type_id) {
      case PropertyType.APARTMENT:
        return "Квартира";
      case PropertyType.ROOM:
        return "Комната";
      case PropertyType.HOUSE:
        return "Дом";
    }
  }

  infoCellFormatter = (cell, row) => {
    return (
      <div className="container">
        <div className="row">
          <Col xs="10" sm="10" lg="3">
            <div><Label>{this.convertPropertyType(row.type_id)} по адресу {row.city}, {row.street} {row.house}</Label>{this.stripNonNumeric(row.apt_price)}</div>
            <div><Label>Площадь (м²):</Label>{row.apt_size}</div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div><Label>Цена (₽):</Label>{this.stripNonNumeric(row.apt_price.toString())}</div>
            <div><Label>Вид из окон:</Label>{this.convertViewFromWindow(row.windows_view)}</div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div><Label>Цена (₽):</Label>{this.stripNonNumeric(row.apt_price)}</div>
            <div><Label>Площадь (м²):</Label>{row.apt_size}</div>
          </Col>
          <Col xs="10" sm="10" lg="3">
            <div><Label>Цена (₽):</Label>{this.stripNonNumeric(row.apt_price)}</div>
            <div><Label>Площадь (м²):</Label>{row.apt_size}</div>
          </Col>

        </div>
      </div>
    )
  }

  sellerCellFormatter = (cell, row) => {
    return (
      <div>
        <div>{row.seller.name}</div>
        <div>{row.description}</div>
      </div>
    )
  }

  render () {
    return (
    <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-filter"/>
                <strong>Расположение</strong>
              </CardHeader>
              <CardBody>
                <Row className="align-items-center">
                  <Col xs="10" sm="10" lg="2">
                    <Input className="float-left col-lg-12" placeholder="Город" type="text" name="geocity" id="geocity"
                           color="success"/>
                  </Col>
                  <Col xs="10" sm="10" lg="3">
                    <Select
                      placeholder="Район"
                      closeMenuOnSelect={false}
                      name="form-city-disctict"
                      value={this.state.selectedDistricts}
                      options={this.state.districtOptions}
                      onChange={this.saveDistrictsChange}
                      isMulti
                      multi
                      styles={{
                        multiValueLabel: base => ({
                          ...base,
                          backgroundColor: 'blue',
                          color: 'white',
                        }),
                      }}
                    />
                  </Col>
                  <Col xs="10" sm="10" lg="3">
                    <Select
                      placeholder="Метро"
                      closeMenuOnSelect={false}
                      name="form-city-subway"
                      value={this.state.selectedSubwayStations}
                      options={this.state.subwayStationOptions}
                      onChange={this.saveSubwayStationsChange}
                      isMulti
                      multi
                      styles={{
                        multiValueLabel: base => ({
                          ...base,
                          backgroundColor: 'blue',
                          color: 'white',
                        }),
                      }}
                    />
                  </Col>
                  <div>Или</div>
                  <Col xs="10" sm="10" lg="3">
                    <Input placeholder="Полный или частичный адрес" className="col-lg-12" type="text" name="geoaddress"
                           id="geoaddress" color="success"/>
                  </Col>
                </Row>

              </CardBody>
            </Card>
          </Col>
        </Row>

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-filter"/>
                <strong>Основные фильтры</strong>
              </CardHeader>
              <CardBody>
                <Row className="align-items-center">
                  <Col xs="12" sm="12" lg="2">
                    <ButtonGroup className="float-right">
                      <ButtonDropdown id='card1' isOpen={this.state.card1} toggle={() => {
                        this.setState({ card1: !this.state.card1 })
                      }}>
                        <DropdownToggle caret className="p-0" color="black">
                          <i className="fa fa-list-ul fa-lg"></i>
                        </DropdownToggle>
                        <DropdownMenu right>
                          <DropdownItem header>Жилая</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="0">Квартира</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="1">Комната</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="2">Доля</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="3">Дом</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="4">Часть дома</DropdownItem>
                          <DropdownItem header>Коммерческая</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="5">Офис</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="6">Торговая площадь</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="7">Склад</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="8">Общепит</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="9">Гараж</DropdownItem>
                          <DropdownItem onClick={this.handleTypeClick} id="10">Автосервис</DropdownItem>
                          {/*<DropdownItem onClick={this.handleTypeClick} id = "9">ПСН</DropdownItem>*/}
                          {/*<DropdownItem onClick={this.handleTypeClick} id = "12">Производство</DropdownItem>*/}
                          {/*<DropdownItem onClick={this.handleTypeClick} id = "14">Готовый бизнес</DropdownItem>*/}
                          {/*<DropdownItem onClick={this.handleTypeClick} id = "15">Здание</DropdownItem>*/}
                          {/*<DropdownItem onClick={this.handleTypeClick} id = "16">Бытовые услуги</DropdownItem>*/}
                        </DropdownMenu>
                      </ButtonDropdown>
                    </ButtonGroup>
                    <div className="h3">{this.state.type_name}</div>
                    <FormText color="muted">
                      тип недвижимости
                    </FormText>
                  </Col>

                  <Col xs="12" sm="12" lg="2">
                    <ButtonGroup className="float-right">
                      <Dropdown id='card2' isOpen={this.state.card2} toggle={() => {
                        this.setState({ card2: !this.state.card2 })
                      }}>
                        <DropdownToggle caret className="p-0" color="black">
                          <i className="fa fa-list-ul fa-lg"></i>
                        </DropdownToggle>
                        <DropdownMenu right>
                          <DropdownItem onClick={this.handleMarketClick} id="1">Любая</DropdownItem>
                          <DropdownItem onClick={this.handleMarketClick} id="2">Вторичка</DropdownItem>
                          <DropdownItem onClick={this.handleMarketClick} id="3">Новостройка</DropdownItem>
                        </DropdownMenu>
                      </Dropdown>
                    </ButtonGroup>
                    <div className="h3">{this.state.market_name}</div>
                    <FormText color="muted">
                      рынок недвижмости
                    </FormText>
                  </Col>

                  <Col xs="12" sm="12" lg="2">
                    <ButtonGroup className="float-right">
                      <Dropdown id='card3' isOpen={this.state.card3} toggle={() => {
                        this.setState({ card3: !this.state.card3 })
                      }}>
                        <DropdownToggle caret className="p-0" color="black">
                          <i className="fa fa-list-ul fa-lg"></i>
                        </DropdownToggle>
                        <DropdownMenu right>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="0"/>
                              <Label className="form-check-label" for="defaultCheck1">Студия</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="1"/>
                              <Label className="form-check-label" for="defaultCheck1">1 Комнатная</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="2"/>
                              <Label className="form-check-label" for="defaultCheck1">2 Комнатная</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="3"/>
                              <Label className="form-check-label" for="defaultCheck1">3 Комнатная</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="4"/>
                              <Label className="form-check-label" for="defaultCheck1">4 Комнатная</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="5"/>
                              <Label className="form-check-label" for="defaultCheck1">5 Комнатная</Label>
                            </div>
                          </DropdownItem>
                          <DropdownItem>
                            <div className="form-check" onClick={this.handleClick}>
                              <Input onClick={this.handleRoomsClick} className="form-check-input" type="checkbox"
                                     value=""
                                     id="6"/>
                              <Label className="form-check-label" for="defaultCheck1">6 Комнатная</Label>
                            </div>
                          </DropdownItem>
                        </DropdownMenu>
                      </Dropdown>
                    </ButtonGroup>
                    <div className="h3">{this.state.rooms}</div>
                    <FormText color="muted">
                      количество комнат
                    </FormText>
                  </Col>

                  <Col xs="12" sm="12" lg="2">
                    <ButtonGroup className="float-left">
                      <InputGroupAddon addonType="prepend">
                        <Input type="text" onChange={this.handlePriceFromChange} placeholder="от ₽" required/>
                        <Input type="text" onChange={this.handlePriceToChange} placeholder="до ₽" required/>
                      </InputGroupAddon>
                    </ButtonGroup>
                    <FormText color="muted">
                      Цена
                    </FormText>
                  </Col>

                  <Col xs="6" sm="6" lg="2">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3" aria-label="First group">
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handleAptSizeFromChange} placeholder="м² от" required/>
                        </InputGroupAddon>
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handleAptSizeToChange} placeholder="м² до" required/>
                        </InputGroupAddon>
                      </ButtonGroup>
                    </ButtonToolbar>
                    <FormText color="muted">
                      Площадь
                    </FormText>
                  </Col>

                  <Col className="col-md-2">
                    <ButtonGroup size="md">
                      <Button onClick={this.handleFilterButtonClick} id="filter" block ghost color="primary"
                              size="md"
                              className="float-left"><i className="fa fa-filter"></i>
                      </Button>
                      <Button onClick={this.handleSearchButtonClick} id="search" size="md" className="float-right"><i
                        className="fa fa-search"></i></Button>
                      <UncontrolledTooltip placement="right" target="filter">
                        Дополнительные фильтры
                      </UncontrolledTooltip>
                      <UncontrolledTooltip placement="right" target="search">
                        Искать
                      </UncontrolledTooltip>
                    </ButtonGroup>
                  </Col>

                </Row>

              </CardBody>
            </Card>
          </Col>
        </Row>

        {this.state.showFilters &&
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-filter"/>
                <strong>Дополнительные фильтры</strong>
              </CardHeader>
              <CardBody>
                <Row className="align-items-center">
                  <Col xs="4" sm="4" lg="1">Вид из окон</Col>
                  <Col xs="6" sm="6" lg="3">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3">
                        <Button color="outline-secondary" onClick={() => this.onWindowRadioBtnClick(1)}
                                active={this.state.windowRadioSelected === 1}>Любой</Button>
                        <Button color="outline-secondary" onClick={() => this.onWindowRadioBtnClick(2)}
                                active={this.state.windowRadioSelected === 2}>Двор</Button>
                        <Button color="outline-secondary" onClick={() => this.onWindowRadioBtnClick(3)}
                                active={this.state.windowRadioSelected === 3}>Улица</Button>
                      </ButtonGroup>
                      <Button color="outline-secondary" onClick={() => this.onBalconRadioBtnClick(true)}
                              active={this.state.balconRadioSelected === true}>Балкон/Лоджия</Button>
                    </ButtonToolbar>
                  </Col>
                  <Col xs="4" sm="4" lg="1">Площадь кухни</Col>
                  <Col xs="6" sm="6" lg="2">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3" aria-label="First group">
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handleKitchenSizeFromChange} placeholder="м² от" required/>
                        </InputGroupAddon>
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handleKitchenSizeToChange} placeholder="м² до" required/>
                        </InputGroupAddon>
                      </ButtonGroup>
                    </ButtonToolbar>
                  </Col>
                  <Col xs="4" sm="4" lg="1">Высота потолков</Col>
                  <Col xs="6" sm="6" lg="3">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3">
                        <Button color="outline-secondary" onClick={() => this.onCeilingRadioBtnClick(1)}
                                active={this.state.ceilingRadioSelected === 1}>Любая</Button>
                        <Button color="outline-secondary" onClick={() => this.onCeilingRadioBtnClick(2)}
                                active={this.state.ceilingRadioSelected === 2}>2.5+</Button>
                        <Button color="outline-secondary" onClick={() => this.onCeilingRadioBtnClick(3)}
                                active={this.state.ceilingRadioSelected === 3}>3.0+</Button>
                        <Button color="outline-secondary" onClick={() => this.onCeilingRadioBtnClick(4)}
                                active={this.state.ceilingRadioSelected === 4}>3.5+</Button>
                        <Button color="outline-secondary" onClick={() => this.onCeilingRadioBtnClick(5)}
                                active={this.state.ceilingRadioSelected === 5}>4.0+</Button>
                      </ButtonGroup>
                    </ButtonToolbar>
                  </Col>
                </Row>

                <Row className="align-items-center">
                  <Col xs="4" sm="4" lg="1">Этаж</Col>
                  <Col xs="6" sm="6" lg="2">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3">
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handlePriceFromClick} placeholder="от" required/>
                        </InputGroupAddon>
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handlePriceFromClick} placeholder="до" required/>
                        </InputGroupAddon>
                      </ButtonGroup>
                    </ButtonToolbar>
                  </Col>
                  <Col xs="4" sm="4" lg="1">Или</Col>
                  <Col xs="6" sm="6" lg="3">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3">
                        <Button color="outline-secondary" onClick={() => this.onNotFirstFloorsRadioBtnClick(true)}
                                active={this.state.notFirstFloorRadioSelected === true}>Не первый</Button>
                        <Button color="outline-secondary" onClick={() => this.onLastOrNotLastFloorsRadioBtnClick(2)}
                                active={this.state.lastOrNotLastFloorsRadioSelected === 2}>Не последний</Button>
                        <Button color="outline-secondary" onClick={() => this.onLastOrNotLastFloorsRadioBtnClick(3)}
                                active={this.state.lastOrNotLastFloorsRadioSelected === 3}>Последний</Button>
                      </ButtonGroup>
                    </ButtonToolbar>
                  </Col>
                  <Col xs="4" sm="4" lg="1">Этажей в доме</Col>
                  <Col xs="6" sm="6" lg="2">
                    <ButtonToolbar className="float-left">
                      <ButtonGroup className="mr-3">
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handlePriceFromClick} placeholder="от" required/>
                        </InputGroupAddon>
                        <InputGroupAddon addonType="prepend">
                          <Input type="text" onChange={this.handlePriceFromClick} placeholder="до" required/>
                        </InputGroupAddon>
                      </ButtonGroup>
                    </ButtonToolbar>
                  </Col>
                </Row>

              </CardBody>
            </Card>
          </Col>
        </Row>
        }

        <Row>
          <Col>
            <br/>
            <BootstrapTable data={this.state.propertiesList} version="4" striped hover pagination search options={this.options}>
              <TableHeaderColumn dataField="image" isKey width='10%'
                                 dataFormat={this.imageCellFormatter}></TableHeaderColumn>
              <TableHeaderColumn dataField="description"
                                 dataFormat={this.infoCellFormatter}>Описание</TableHeaderColumn>
            </BootstrapTable>
          </Col>
        </Row>
      </div>
    )
  }
}

export default inject('appStore')(observer(Dashboard))
