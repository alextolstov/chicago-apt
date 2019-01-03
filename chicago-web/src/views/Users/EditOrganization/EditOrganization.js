import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {inject, observer} from "mobx-react/index";
import {
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row,
} from 'reactstrap';
import {AppSwitch} from '@coreui/react'
// React select
import Select from 'react-select';
import 'react-select/dist/react-select.min.css';
import UserApi from '../../../api/UserApi';
import FormApi from '../../../api/FormApi';
import PositionApi from '../../../api/PositionApi';
import PermissionApi from '../../../api/PermissionApi';
import DateTimeApi from '../../../api/DateTimeApi';
import AddressForm from '../../Forms/AddressForm/AddressForm';
import PositionForm from '../../Forms/PositionForm/PositionForm';
import PermissionForm from '../../Forms/PermissionForm/PermissionForm';
import RegisterForm from '../../Forms/RegisterForm/RegisterForm';
import {ToastContainer, toast} from 'react-toastify';





class EditOrganization extends Component {
  constructor(props) {
    super(props);
  };


  render() {
    return (
      <div>
        Форма организации
      </div>
    );
  }  
}

export default inject("appStore")(observer(EditOrganization));
