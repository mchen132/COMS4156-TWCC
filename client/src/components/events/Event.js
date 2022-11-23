import React from 'react';
import Card from 'react-bootstrap/Card';

const Event = ({ event }) => {
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

    return (
        <Card style={{ width: '18rem' }}>
            <Card.Body>
                <Card.Title>{name}</Card.Title>
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