import React, {Component} from 'react';
import {Redirect, Route} from 'react-router-dom';
import 'spinkit/css/spinkit.css';
import Login from "../views/Pages/Login";

class PrivateRoute extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isAuthenticated: false,
      isLoading: true
    }
  }

  componentDidMount() {
    fetch('/api/login/testauth', {
      method: "GET",
      credentials: 'include'
    }).then(response => {
        (response.ok) ? this.setState({isAuthenticated: true}) : this.setState({isAuthenticated: false});
        this.setState({isLoading: false});
      }
    );
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

export default PrivateRoute;
