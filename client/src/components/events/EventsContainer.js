import React from 'react';
import Events from './Events';
import { getAuthInformation } from '../../utils/authUtil';
import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import '../../styles/events.css';

const EventsContainer = () => {
    return (
        <>
            <div className='events-container-header'>
                {
                        getAuthInformation('token')
                            ? <h2>Welcome {getAuthInformation('username')}!</h2>
                            : <>
                                <h2>Login to view events</h2>
                                <Link to="/login"><Button variant='primary'>Login</Button></Link>
                            </>
                }
            </div>
            <div className="events-container">
                <Events />
            </div>
        </>
    );
};

export default EventsContainer;