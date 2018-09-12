import React, {Component} from 'react';
import {Redirect, Route} from 'react-router-dom';
import 'spinkit/css/spinkit.css';
import UserApi from '../api/UserApi';
import {inject} from "mobx-react/index";

class PrivateRoute extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isAuthenticated: false,
      isLoading: true
    }
  }

  componentDidMount() {
    let self = this;
    let userApi = new UserApi();

    userApi.testAuth().then(function (user_id) {
      if (user_id != null) {
        // Session still alive but is user info left? If not lets take it from server
        if (self.props.appStore.userData.getUserId() == "") {
          userApi.getUserById(user_id, null).then(function (user) {
            if (user != null) {
              self.props.appStore.userData = user;
              window.sessionStorage.setItem("current_user", user.getUserId());
            }
          });
        }
        self.setState({isAuthenticated: true})
      }
      else {
        self.setState({isAuthenticated: false});
      }
      self.setState({isLoading: false});
    });
  }

  render() {
    const { isAuthenticated, isLoading } = this.state;

    if (isLoading) {
      return (
        <div className="container">
          <div className="centered sk-circle">
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
        </div>);
    }

    if (isAuthenticated === false) {
      return (
        <Redirect to="/login"/>
      );
    } else {
      return (
        <Route {...this.props}/>
      );
    }
  }
}

export default inject("appStore")(PrivateRoute);
