import React from 'react';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { deleteEvent } from '../../actions/eventActions';
import { getAuthInformation } from '../../utils/authUtil';

const Event = ({ event, onDeleteEventCallback }) => {
    const { 
        id,
        name,
        description,
        address,
        ageLimit,
        cost,
        creationTimestamp,
        endTimestamp,
        latitude,
        longitude,
        media,
        startTimestamp,
        host
    } = event;

    const userId = getAuthInformation('userId');

    const onDeleteEvent = async () => {
        try {
            const res = await deleteEvent(id);
            onDeleteEventCallback(res);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <Card className='event-card' style={{ width: '18rem' }}>
            {
                host && parseInt(userId) === parseInt(host) && 
                    <Button variant='danger' onClick={onDeleteEvent}>Delete Event</Button>
            }
            <Card.Body>
                <Card.Title>{name}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">Host: {host}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Address: {address}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Age Restriction: {ageLimit}+</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Cost: ${cost}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Starts: {startTimestamp}, Ends: {endTimestamp}</Card.Subtitle>
                <Card.Text>About: {description}</Card.Text>
                <Card.Link href={media}>Learn more</Card.Link>
            </Card.Body>
        </Card>
    );
};

export default Event;