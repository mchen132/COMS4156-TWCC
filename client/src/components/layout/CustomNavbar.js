import React from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

const CustomNavbar = ({ home, title, leftLinks, rightLinks }) => {
    const renderLeftLinks = () => (
        leftLinks.map((link, index) => 
            <Nav.Link
                key={`${link.name.replace(/\s+/g, '')}-${index}`}
                href={link.href && `/${link.href}`}
                onClick={link.onClick}
            >
                {link.name}
            </Nav.Link>
        )
    );

    const renderRightLinks = () => (
        rightLinks.map((link, index) => 
            <Nav.Link
                key={`${link.name.replace(/\s+/g, '')}-${index}`}
                href={link.href && `/${link.href}`}
                onClick={link.onClick}
            >
                {link.name}
            </Nav.Link>
        )
    );

    return (
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand href={home}>{ title }</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    {renderLeftLinks()}
                </Nav>
                <Nav>
                    {renderRightLinks()}
                </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
      );
};

export default CustomNavbar;