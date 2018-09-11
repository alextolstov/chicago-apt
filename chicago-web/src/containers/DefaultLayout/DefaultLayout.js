import React, {Component} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import {Container} from 'reactstrap';
import {inject, observer} from "mobx-react/index";
import {
  AppAside,
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  AppSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from '../../_nav';
// routes config
import routes from '../../routes';

import DefaultAside from './DefaultAside';
import DefaultFooter from './DefaultFooter';
import DefaultHeader from './DefaultHeader';
import UserApi from "../../api/UserApi";

const user_proto = require('models/user_pb');

class DefaultLayout extends Component {
  constructor(props) {
    super(props);
    this.getUser();
  }

  handleError(error) {
    // TODO finish the function if neded
  }

  getUser() {
    if (this.props.appStore.userData.getUserId() == "") {
      let self = this;
      let user_id = window.sessionStorage.getItem("current_user");
      this.props.appStore.userData = new UserApi().getUserById(user_id, this).then(function (user) {
        if (user != null) {
          self.props.appStore.userData = user;
        }
      })
    }
  }

  render() {
    return (
      <div className="app">
        <AppHeader fixed>
          <DefaultHeader/>
        </AppHeader>
        <div className="app-body">
          <AppSidebar fixed display="lg">
            <AppSidebarHeader/>
            <AppSidebarForm/>
            <AppSidebarNav navConfig={navigation} {...this.props} />
            <AppSidebarFooter/>
            <AppSidebarMinimizer/>
          </AppSidebar>
          <main className="main">
            <AppBreadcrumb appRoutes={routes}/>
            <Container fluid>
              <Switch>
                {routes.map((route, idx) => {
                    return route.component ? (
                        <Route key={idx} path={route.path} exact={route.exact} name={route.name} render={props => (
                          <route.component {...props} />
                        )}/>)
                      : (null);
                  },
                )}
                <Redirect from="/" to="/dashboard"/>
              </Switch>
            </Container>
          </main>
          <AppAside fixed hidden>
            <DefaultAside/>
          </AppAside>
        </div>
        <AppFooter>
          <DefaultFooter/>
        </AppFooter>
      </div>
    );
  }
}

export default inject("appStore")(observer(DefaultLayout));
