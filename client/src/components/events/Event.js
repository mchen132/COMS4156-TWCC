import React, { useState } from 'react';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { deleteEvent, updateEvent } from '../../actions/eventActions';
import { getAuthInformation } from '../../utils/authUtil';
import Modal from 'react-bootstrap/Modal';
import DateTimePicker from 'react-datetime-picker';
import moment from 'moment';

const Event = ({ event, onDeleteEventCallback }) => {
    const [localEvent, setLocalEvent] = useState(event);
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
        host,
        categories
    } = localEvent;

    const [showUpdateEventModal, setShowUpdateEventModal] = useState(false);
    const [updateEventData, setUpdateEventData] = useState({
        ...event,
        startTimestamp: moment(event.startTimestamp).toDate(),
        endTimestamp: moment(event.endTimestamp).toDate()
    });

    const userId = getAuthInformation('userId');

    const onUpdateEvent = async () => {
        console.log("update");
        try {
            // Format datetimes
            const eventToUpdate = {
                ...updateEventData,
                startTimestamp: updateEventData.startTimestamp && moment(updateEventData.startTimestamp).utc().format("yyyy-MM-DD HH:mm:ss.SS"),
                endTimestamp: updateEventData.endTimestamp && moment(updateEventData.endTimestamp).utc().format("yyyy-MM-DD HH:mm:ss.SS")
            };
            console.log(eventToUpdate);
            const updatedEvent = await updateEvent(eventToUpdate);
            console.log("updated event");
            setLocalEvent(updatedEvent);
            setShowUpdateEventModal(false);
        } catch (err) {
            console.error('Failed to update event');
        }
    };
    
    const onDeleteEvent = async () => {
        try {
            const res = await deleteEvent(id);
            onDeleteEventCallback(res);
        } catch (err) {
            console.error(err);
        }
    };

    const onUpdateEventModalChange = e => {
        setUpdateEventData({ ...updateEventData, [e.target.name]: e.target.value });
    };

    const renderUpdateEventModal = () => {
        return (
            <Modal show={showUpdateEventModal} onHide={() => setShowUpdateEventModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form className="form">
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Address"
                                name="address"
                                value={updateEventData.address}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Age Limit"
                                name="ageLimit"
                                value={updateEventData.ageLimit}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Name"
                                name="name"
                                value={updateEventData.name}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Description"
                                name="description"
                                value={updateEventData.description}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Longitude"
                                name="longitude"
                                value={updateEventData.longitude}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Latitude"
                                name="latitude"
                                value={updateEventData.latitude}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Cost"
                                name="cost"
                                value={updateEventData.cost}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Media"
                                name="media"
                                value={updateEventData.media}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Category1,category2,category3..."
                                name="categories"
                                value={updateEventData.categories}
                                onChange={e => onUpdateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <DateTimePicker
                                format="yyyy-MM-dd HH:mm:ss"
                                onChange={dateTime => setUpdateEventData(
                                    { ...updateEventData, startTimestamp: dateTime }
                                )}
                                value={updateEventData.startTimestamp}
                            />
                        </div>
                        <div className="form-group">
                            <DateTimePicker
                                format="yyyy-MM-dd HH:mm:ss"
                                onChange={dateTime => setUpdateEventData(
                                    { ...updateEventData, endTimestamp: dateTime }
                                )}
                                value={updateEventData.endTimestamp}
                            />
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowUpdateEventModal(false)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={onUpdateEvent}>
                        Update Event
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    };

    return (
        <Card className='event-card' style={{ width: '18rem' }}>
            {
                host && parseInt(userId) === parseInt(host) && 
                    <Button variant='outline-primary' onClick={() => setShowUpdateEventModal(true)}>Update Event</Button>
            }
            {renderUpdateEventModal()}
            <Card.Body>
                <Card.Title>{name}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">Host: {host}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Categories: {categories}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Address: {address}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Age Restriction: {ageLimit}+</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Cost: ${cost}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Starts: {startTimestamp && moment(startTimestamp).format("yyyy-MM-DD h:mm:ss A")}</Card.Subtitle>
                <Card.Subtitle className="mb-2 text-muted">Ends: {endTimestamp && moment(endTimestamp).format("yyyy-MM-DD h:mm:ss A")}</Card.Subtitle>
                <Card.Text>About: {description}</Card.Text>
                <Card.Link target={"_blank"} href={(media && media.includes("http://")) ? media : `http://${media}`}>Learn more</Card.Link>
            </Card.Body>
            {
                host && parseInt(userId) === parseInt(host) && 
                    <Button variant='outline-danger' onClick={onDeleteEvent}>Delete Event</Button>
            }
        </Card>
    );
};

export default Event;