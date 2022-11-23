import React from 'react';
import Events from './Events';
import { getAuthInformation } from '../../utils/authUtil';

const EventsContainer = () => {
    return (
        <div className="events-container">
            <h2>Welcome {getAuthInformation('username')}!</h2>
            <Events />
        </div>
    );
};

export default EventsContainer;