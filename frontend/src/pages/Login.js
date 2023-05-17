import React from "react";
import Form from "react-bootstrap/Form";
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faLock, faSun, faRightToBracket } from "@fortawesome/free-solid-svg-icons";


export default class Login extends React.Component {

    render() {
        return (
            <div className="row">
            <div className="d-none d-lg-block d-lg-block col-lg-3"></div>
            
            <div className="login col col-lg-6 d-flex flex-column min-vh-100 g-5">
            <div className="mx-auto my-auto align-middle w-100 rounded p-3 border shadow">
                <div className="text-warning fs-3 text-center mb-3 border-bottom pb-3">
                    <FontAwesomeIcon icon={ faSun } className="fa-fw fa-spin me-1" />
                    Sunny Holidays
                </div>
                <Form>
                    <InputGroup className="mb-2">
                        <InputGroup.Text>
                            <FontAwesomeIcon icon={faUser} className="fa-fw" />
                        </InputGroup.Text>
                        <FloatingLabel
                            controlId="usernameInput"
                            label="Username"
                        >
                            <Form.Control type="text" placeholder="Username"/>
                        </FloatingLabel>
                    </InputGroup>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>
                            <FontAwesomeIcon icon={faLock} className="fa-fw" />
                        </InputGroup.Text>
                        <FloatingLabel
                            controlId="usernameInput"
                            label="Password"
                        >
                            <Form.Control type="password" placeholder="Password"/>
                        </FloatingLabel>
                    </InputGroup>
                    <Button type="submit" variant="outline-warning" className="w-100 mb-2">
                        <FontAwesomeIcon icon={faRightToBracket} className="fa-fw me-1" />
                        Sign in
                    </Button>
                </Form>
            </div>
            </div>
            
            </div>
        )
    }
}