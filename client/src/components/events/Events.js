import React, { useEffect, useState } from 'react';
import Event from './Event';

const Events = ({ events }) => {
    const [localEvents, setLocalEvents] = useState(events);

    useEffect(() => {
        setLocalEvents(events);
    }, [events]);

    const removeEvent = (eventId) => {
        setLocalEvents(localEvents.filter(event => event.id !== eventId));
    }

    return (
        <>
            { localEvents && localEvents.length > 0 && localEvents.map(event => <Event key={event.id} event={event} onDeleteEventCallback={removeEvent}/>) }
        </>
    );
};

export default Events;