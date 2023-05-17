import React from 'react';
import { Container } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faYoutube, faGithub, faFacebookF } from '@fortawesome/free-brands-svg-icons';
import { faRegistered } from '@fortawesome/free-solid-svg-icons';

import FooterLink from './FooterLink';


const Footer = () => {
    return (
        <footer className="py-3 px-3 border-top mt-3">
        <Container fluid="xl">
        <div className="d-flex justify-content-between">
            <div className="d-flex align-items-center">
                <FontAwesomeIcon icon={ faRegistered } className="align-middle fa-fw me-1"/>
                <span className="align-middle">RSWW Project</span>
            </div>
            <div className="d-flex justify-content-evenly justify-content-xl-between">
                <FooterLink href="#" icon={faYoutube} bgColor={"#ED302F"} />
                <FooterLink href="#" icon={faFacebookF} bgColor={"#3B5998"} />
                <FooterLink href="#" icon={faGithub} bgColor={"black"} />
            </div>
        </div>
        </Container>
        </footer>
    )
}


export default Footer;