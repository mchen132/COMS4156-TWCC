import React from 'react';
import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';

const Landing = () => {
  return (
    <section className="landing">
        <div className="landing-inner">
            <p className="lead">
                Learn about events
            </p>
            <div className="buttons">
                <Link to="/register"><Button variant="primary">Sign Up</Button></Link>
                <Link to="/login"><Button variant="light">Login</Button></Link>
            </div>
        </div>
    </section>
  )
};

export default Landing;
