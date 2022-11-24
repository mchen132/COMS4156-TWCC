import React, { useEffect, useState } from 'react';
import Event from './Event';
import { getEvents } from '../../actions/eventActions';

const Events = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const events = await getEvents();
                console.log(events);
                setEvents(events);
            } catch (err) {
                console.error(err);
            }
        };

        fetchEvents();
    }, []);

    return (
        <>
            { events && events.length > 0 && events.map(event => <Event key={event.id} event={event}/>) }
        </>
    );
};

export default Events;