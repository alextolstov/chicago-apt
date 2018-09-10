import React, {Component} from 'react';
import {HashRouter, Route, Switch} from 'react-router-dom';
import {Provider} from 'mobx-react';
import {AppStore, PrivateRoute} from './components';
// Styles
import './App.css';
// CoreUI Icons Set
import '@coreui/icons/css/coreui-icons.min.css';
// Import Flag Icons Set
import 'flag-icon-css/css/flag-icon.min.css';
// Import Font Awesome Icons Set
import 'font-awesome/css/font-awesome.min.css';
// Import Simple Line Icons Set
import 'simple-line-icons/css/simple-line-icons.css';
// Import Main styles for this application
import './scss/style.css'
// Containers
import {DefaultLayout} from './containers';
// Pages
import {Login, Page404, Page500, Register} from './views/Pages';

// import { renderRoutes } from 'react-router-config';
const appStore = new AppStore();

class App extends Component {

  render() {
    return (
      <Provider appStore={appStore}>
        <div className="App">
          <HashRouter>
            <Switch>
              <Route exact path="/login" name="Login Page" component={Login}/>
              <Route exact path="/register" name="Register Page" component={Register}/>
              <Route exact path="/404" name="Page 404" component={Page404}/>
              <Route exact path="/500" name="Page 500" component={Page500}/>
              <PrivateRoute path="/" name="Home" component={DefaultLayout}/>
            </Switch>
          </HashRouter>
        </div>
      </Provider>
    );
  }
}

export default App;
