import React, {Component} from 'react';
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
  Progress,
  Row,
  Table,
  UncontrolledTooltip,
} from 'reactstrap';
import {inject, observer} from 'mobx-react';
import UiSearchFilters from "../../models/UiSearchFilters";
import Select from 'react-select';
import CityApi from '../../api/CityApi';
import SearchApi from '../../api/SearchFiltersApi';

class Dashboard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      type_name: "Квартира",
      market_name: "Любой",
      rooms: "Любое",
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
      subwayStationOptions: []
    };

    this.uiParameters = new UiSearchFilters();
    this.cityApi = new CityApi();
    this.searchApi = new SearchApi();
  }

  componentDidMount() {
    let self = this;
    const $ = window.$;
    $("#geoaddress").suggestions({
      token: "6c595d7dcead327d7c7b5a1d74d37ba7291428c6",
      type: "ADDRESS",
      /* Вызывается, когда пользователь выбирает одну из подсказок */
      onSelect: function (suggestion) {
        self.uiParameters.city_id = suggestion.data.city_fias_id;
        console.log(suggestion);
      }
    });

    $("#geocity").suggestions({
      token: "6c595d7dcead327d7c7b5a1d74d37ba7291428c6",
      type: "ADDRESS",
      hint: false,
      bounds: "city",
      constraints: {
        label: "",
        locations: {city_type_full: "город"}
      },      /* Вызывается, когда пользователь выбирает одну из подсказок */
      onSelect: function (suggestion) {
        self.uiParameters.city_id = suggestion.data.city_fias_id;
        console.log(suggestion);
        // Call server
        self.cityApi.getCity(self.uiParameters.city_id, (e) => {
            console.log('Error load:', e)
          }
        ).then(function (obj) {
          // Districts
          let districtOptMap = new Map();
          for (let d of obj.districts) {
            let valueArr = districtOptMap.get(d.district_classifier);
            if (valueArr === undefined) {
              valueArr = new Array();
            }
            valueArr.push({district_id: d.district_id, district_name: d.district_name});
            districtOptMap.set(d.district_classifier, valueArr);
          }

          let districtOpt = new Array();
          districtOptMap.forEach(function (val, key) {
            let classifier = {label: key, options: []};
            for (let v of val) {
              classifier.options.push({
                value: v.district_id,
                label: v.district_name
              });
            }
            districtOpt.push(classifier);
          });
          self.setState({districtOptions: districtOpt});

          // Subway Stations
          let subwayStationOpt = new Array();
          for (let sl of obj.subway_lines) {
            let line = {label: sl.line_name, options: []};
            for (let ss of sl.subway_stations) {
              line.options.push({
                value: ss.station_id,
                label: ss.station_name
              })
            }
            subwayStationOpt.push(line);
          }
          self.setState({subwayStationOptions: subwayStationOpt});
        });
      }
    });
  }

  toggle = () => {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen,
    });
  }

  onWindowRadioBtnClick = (radioSelected) => {
    this.uiParameters.windows_view = radioSelected;
    this.setState({windowRadioSelected: radioSelected});
  }

  onCeilingRadioBtnClick = (radioSelected) => {
    this.uiParameters.ceiling_height = radioSelected;
    this.setState({ceilingRadioSelected: radioSelected});
  }

  onNotFirstFloorsRadioBtnClick = (radioSelected) => {
    this.uiParameters.not_first_floor = !this.state.notFirstFloorRadioSelected;
    this.setState({notFirstFloorRadioSelected: !this.state.notFirstFloorRadioSelected});
  }

  onLastOrNotLastFloorsRadioBtnClick = (radioSelected) => {
    if (radioSelected === 2) {
      this.uiParameters.not_last_floor = true;
      this.uiParameters.last_floor = false;
    } else {
      this.uiParameters.last_floor = true;
      this.uiParameters.not_last_floor = false;
    }
    this.setState({lastOrNotLastFloorsRadioSelected: radioSelected});
  }

  onBalconRadioBtnClick = (radioSelected) => {
    this.uiParameters.balcony = !this.state.balconRadioSelected;
    this.setState({balconRadioSelected: !this.state.balconRadioSelected});
  }

  handleClick = (e) => {
    e.stopPropagation();
  }

  handleTypeClick = (e) => {
    this.uiParameters.type_id = e.target.attributes.id;
    this.setState({type_name: e.nativeEvent.target.innerText});
  }

  handleMarketClick = (e) => {
    this.uiParameters.market_id = e.target.attributes.id;
    this.setState({market_name: e.nativeEvent.target.innerText});
  }

  handleRoomsClick = (e) => {
    let clicked_rooms = parseInt(e.target.attributes.id.value, 10);
    if (this.uiParameters.rooms_number.has(clicked_rooms)) {
      this.uiParameters.rooms_number.delete(clicked_rooms);
    } else {
      this.uiParameters.rooms_number.add(clicked_rooms);
    }

    var show_rooms = [...this.uiParameters.rooms_number].join(',');
    if (show_rooms === "") {
      show_rooms = "Любое";
    }
    this.setState({rooms: show_rooms});
  }

  handlePriceFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.apt_price_from = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handlePriceToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.apt_price_to = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handleAptSizeFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.apt_size_from = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handleAptSizeToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.apt_size_to = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handleKitchenSizeFromChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.kitchen_size_from = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handleKitchenSizeToChange = (e) => {
    e.target.value = this.stripNonNumeric(e.target.value);
    this.uiParameters.kitchen_size_to = parseInt(e.target.value.replace(/\D/g, ''));
  }

  handleFilterButtonClick = (e) => {
    this.setState({showFilters: !this.state.showFilters});
  }

  handleSearchButtonClick = (e) => {
    console.log(this.uiParameters);
    this.searchApi.search(this.uiParameters, (e) => {
      console.log('Error load:', e)
    })
      .then(function (obj) {
        console.log(obj);
      });
  }

  saveDistrictsChange = (selectedDistricts) => {
    for (let d of selectedDistricts) {
      this.uiParameters.district_id.push(d.value);
    }
    this.setState({selectedDistricts});
  }

  saveSubwayStationsChange = (selectedSubwayStations) => {
    for (let s of selectedSubwayStations) {
      this.uiParameters.subway_station_id.push(s.value);
    }
    this.setState({selectedSubwayStations});
  }

  stripNonNumeric = (strValue) => {
    var validChars = /[0-9]/;
    var strIn = strValue;
    var strOut = '';
    for (let i = 0; i < strIn.length; i++) {
      strOut += (validChars.test(strIn.charAt(i))) ? strIn.charAt(i) : '';
    }
    return strOut.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }


  render() {

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
                        this.setState({card1: !this.state.card1});
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
                        this.setState({card2: !this.state.card2});
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
                        this.setState({card3: !this.state.card3});
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
            <Table hover responsive className="table-outline mb-0 d-none d-sm-table">
              <thead className="thead-light">
              <tr>
                <th className="text-center"><i className="icon-people"></i></th>
                <th>User</th>
                <th className="text-center">Country</th>
                <th>Usage</th>
                <th className="text-center">Payment Method</th>
                <th>Activity</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/1.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-success"></span>
                  </div>
                </td>
                <td>
                  <div>Yiorgos Avraamu</div>
                  <div className="small text-muted">
                    <span>New</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-us h4 mb-0" title="us" id="us"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>50%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="success" value="50"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-cc-mastercard" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>10 sec ago</strong>
                </td>
              </tr>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/2.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-danger"></span>
                  </div>
                </td>
                <td>
                  <div>Avram Tarasios</div>
                  <div className="small text-muted">

                    <span>Recurring</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-br h4 mb-0" title="br" id="br"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>10%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="info" value="10"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-cc-visa" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>5 minutes ago</strong>
                </td>
              </tr>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/3.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-warning"></span>
                  </div>
                </td>
                <td>
                  <div>Quintin Ed</div>
                  <div className="small text-muted">
                    <span>New</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-in h4 mb-0" title="in" id="in"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>74%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="warning" value="74"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-cc-stripe" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>1 hour ago</strong>
                </td>
              </tr>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/4.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-secondary"></span>
                  </div>
                </td>
                <td>
                  <div>Enéas Kwadwo</div>
                  <div className="small text-muted">
                    <span>New</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-fr h4 mb-0" title="fr" id="fr"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>98%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="danger" value="98"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-paypal" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>Last month</strong>
                </td>
              </tr>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/5.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-success"></span>
                  </div>
                </td>
                <td>
                  <div>Agapetus Tadeáš</div>
                  <div className="small text-muted">
                    <span>New</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-es h4 mb-0" title="es" id="es"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>22%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="info" value="22"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-google-wallet" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>Last week</strong>
                </td>
              </tr>
              <tr>
                <td className="text-center">
                  <div className="avatar">
                    <img src={'assets/img/avatars/6.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
                    <span className="avatar-status badge-danger"></span>
                  </div>
                </td>
                <td>
                  <div>Friderik Dávid</div>
                  <div className="small text-muted">
                    <span>New</span> | Registered: Jan 1, 2015
                  </div>
                </td>
                <td className="text-center">
                  <i className="flag-icon flag-icon-pl h4 mb-0" title="pl" id="pl"></i>
                </td>
                <td>
                  <div className="clearfix">
                    <div className="float-left">
                      <strong>43%</strong>
                    </div>
                    <div className="float-right">
                      <small className="text-muted">Jun 11, 2015 - Jul 10, 2015</small>
                    </div>
                  </div>
                  <Progress className="progress-xs" color="success" value="43"/>
                </td>
                <td className="text-center">
                  <i className="fa fa-cc-amex" style={{fontSize: 24 + 'px'}}></i>
                </td>
                <td>
                  <div className="small text-muted">Last login</div>
                  <strong>Yesterday</strong>
                </td>
              </tr>
              </tbody>
            </Table>
          </Col>
        </Row>
      </div>
    );
  }
}

export default inject("appStore")(observer(Dashboard));
