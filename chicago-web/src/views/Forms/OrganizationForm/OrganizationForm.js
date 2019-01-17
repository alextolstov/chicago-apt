import React, {Component} from 'react';
import {defineMessages, FormattedMessage} from 'react-intl';
import {CardBody, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText,} from 'reactstrap';
import ReactPhoneInput from 'react-phone-input-2' 

    
const messages = defineMessages({
    name: {
        id: 'organization.name',
        defaultMessage: 'Name'
    }
});

class RegisterForm extends Component {

    constructor(props) {
        super(props);
        this.state={
            phone:""
        };
        this.handleChangePhone=this.handleChangePhone.bind(this);
    }


      
  render() {
        return (
            <CardBody>
                {/*Name*/}
                <FormGroup row>
                    <InputGroup>
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <i className="fa fa-envelope-o"></i>
                            </InputGroupText>
                        </InputGroupAddon>
                        <FormattedMessage {...messages.name}>
                            {
                                pholder => <Input onChange={this.props.handleChange}
                                    type="text" id="new_name" name="new_name" placeholder={pholder} required/>
                            }
                        </FormattedMessage>
                    </InputGroup>
                </FormGroup>
            </CardBody>
        );
    }
}

export default RegisterForm;
