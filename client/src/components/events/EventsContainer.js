import React from 'react';
import { getAuthInformation } from '../../utils/authUtil';

const EventsContainer = () => {
    return (
        <div className="events-container">
            <h2>Welcome {getAuthInformation('username')}!</h2>
        </div>
    );
};

export default EventsContainer;