import React, { useEffect, useState } from 'react';
import Events from './Events';
import { getAuthInformation } from '../../utils/authUtil';
import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import DateTimePicker from 'react-datetime-picker';
import '../../styles/events.css';
import { getEvents, createEvent } from '../../actions/eventActions';
import moment from 'moment';

const EventsContainer = () => {
    const [showCreateEventModal, setShowCreateEventModal] = useState(false);
    const [createEventData, setCreateEventData] = useState({
        address: '',
        ageLimit: null,
        name: '',
        description: '',
        longitude: null,
        latitude: null,
        cost: null,
        media: '',
        startTimestamp: new Date(),
        endTimestamp: new Date()
    });
    const { 
        address,
        ageLimit,
        name,
        description,
        longitude,
        latitude,
        cost,
        media,
        startTimestamp,
        endTimestamp
    } = createEventData;

    const [localEvents, setLocalEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const currEvents = await getEvents();
                console.log(currEvents);
                setLocalEvents(currEvents);
            } catch (err) {
                console.error(err);
            }
        };

        fetchEvents();
    }, []);

    const onCreateEvent = async e => {
        e.preventDefault();
        console.log("Create event!");
        try {
            // Format datetimes
            const eventToCreate = {
                ...createEventData,
                startTimestamp: moment(startTimestamp).utc().format("yyyy-MM-DDTHH:mm:ss.SSZZ"),
                endTimestamp: moment(endTimestamp).utc().format("yyyy-MM-DDTHH:mm:ss.SSZZ")
            };
            const newEvent = await createEvent(eventToCreate);
            console.log(newEvent);
            setLocalEvents([ ...localEvents, newEvent ]);
            setShowCreateEventModal(false);
        } catch (err) {
            console.error("Failure creating event");
            console.error(err);
        }
    };

    const onCreateEventModalChange = e => {
        setCreateEventData({ ...createEventData, [e.target.name]: e.target.value });
    };

    const renderCreateEventModal = () => {
        return (
            <Modal show={showCreateEventModal} onHide={() => setShowCreateEventModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Create Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form className="form">
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Address"
                                name="address"
                                value={address}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Age Limit"
                                name="ageLimit"
                                value={ageLimit}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Name"
                                name="name"
                                value={name}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Description"
                                name="description"
                                value={description}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Longitude"
                                name="longitude"
                                value={longitude}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Latitude"
                                name="latitude"
                                value={latitude}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                placeholder="Cost"
                                name="cost"
                                value={cost}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                placeholder="Media"
                                name="media"
                                value={media}
                                onChange={e => onCreateEventModalChange(e)}
                            />
                        </div>
                        <div className="form-group">
                            <DateTimePicker
                                format="yyyy-MM-dd HH:mm:ss"
                                onChange={dateTime => setCreateEventData(
                                    { ...createEventData, startTimestamp: dateTime }
                                )}
                                value={startTimestamp}
                            />
                        </div>
                        <div className="form-group">
                            <DateTimePicker
                                format="yyyy-MM-dd HH:mm:ss"
                                onChange={dateTime => setCreateEventData(
                                    { ...createEventData, endTimestamp: dateTime }
                                )}
                                value={endTimestamp}
                            />
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowCreateEventModal(false)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={onCreateEvent}>
                        Create Event
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    };

    return (
        <>
            <div className='events-container-header'>
                {
                        getAuthInformation('token')
                            ? <>
                                <h1>Events</h1>
                                <h2>Welcome {getAuthInformation('username')}!</h2>
                                <Button variant="primary" onClick={() => setShowCreateEventModal(true)}>
                                    Create Event
                                </Button>
                                {renderCreateEventModal()}
                            </>
                            : <>                        
                                <h2>Login to view events</h2>
                                <Link to="/login"><Button variant='primary'>Login</Button></Link>
                            </>
                }
            </div>
            <div className="events-container">
                <Events events={localEvents} />
            </div>
        </>
    );
};

export default EventsContainer;