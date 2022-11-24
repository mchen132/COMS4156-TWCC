import React, { useEffect, useState } from 'react';
import Event from './Event';

const Events = ({ events }) => {
    return (
        <>
            { events && events.length > 0 && events.map(event => <Event key={event.id} event={event}/>) }
        </>
    );
};

export default Events;