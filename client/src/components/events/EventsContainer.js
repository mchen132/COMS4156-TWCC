import React, { useEffect, useState } from 'react';
import Events from './Events';
import { getAuthInformation } from '../../utils/authUtil';
import { Link } from 'react-router-dom';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Accordion from 'react-bootstrap/Accordion';
import Select from 'react-select';
import DateTimePicker from 'react-datetime-picker';
import '../../styles/events.css';
import { getEvents, createEvent, filterEvents } from '../../actions/eventActions';
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
    const [filterEventsData, setFilterEventsData] = useState({});
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const currEvents = await getEvents();
                console.log(currEvents);
                setLocalEvents(currEvents);

                // Set Categories in filterEventsData
                let allCategories = [];
                currEvents.forEach(event => {
                    if (event.categories) {
                        event.categories.split(',').forEach(category => {
                            let trimmedCategory = category.trim();
                            let categoryOption = {
                                value: trimmedCategory,
                                label: trimmedCategory.charAt(0).toUpperCase() + trimmedCategory.slice(1)
                            };

                            if (!allCategories.some(currCategoryOption => currCategoryOption.value === categoryOption.value)) {
                                allCategories.push(categoryOption);
                            }
                        });
                    }
                });

                setCategories(allCategories);
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

    const onFilterEventsDataChange = e => {
        console.log(e);
        setFilterEventsData({
            ...filterEventsData,
            [e.target.name]: e.target.value
        })
    };

    const onFilterEvents = async e => {
        e.preventDefault();

        try {
            const filterEventsQueryParams = {};
            for (const queryParam in filterEventsData) {
                const queryValue = filterEventsData[queryParam];

                if (queryValue && queryValue.length > 0) {
                    filterEventsQueryParams[queryParam] = queryValue;
                }
            }

            const filteredEvents = await filterEvents(filterEventsQueryParams);
            
            setLocalEvents(filteredEvents);
        } catch (err) {
            console.error(err);
        }
    };

    const renderFilterEventsSection = () => (
        <Accordion>
            <Accordion.Item eventKey="0">
                <Accordion.Header>Filter Events</Accordion.Header>
                <Accordion.Body>
                    <Form onSubmit={onFilterEvents}>
                        <Row>
                            <Form.Group as={Col}>
                                <Form.Label>Name</Form.Label>
                                <Form.Control
                                    name="name"
                                    placeholder="Name"                                                        
                                    onChange={onFilterEventsDataChange}
                                />
                            </Form.Group>
                            <Form.Group as={Col}>
                                <Form.Label>Description</Form.Label>
                                <Form.Control 
                                    name="description"
                                    placeholder="Description"
                                    onChange={onFilterEventsDataChange}
                                />                                                
                            </Form.Group>
                        </Row>
                        <Row>
                            <Form.Group as={Col}>
                                <Form.Label>Address</Form.Label>
                                <Form.Control
                                    name="address"
                                    placeholder="Address"
                                    onChange={onFilterEventsDataChange}
                                />                                                    
                            </Form.Group>
                            <Form.Group as={Col}>
                                <Form.Label>Age Limit</Form.Label>
                                <Form.Control
                                    name="ageLimit"
                                    placeholder="Age Limit"
                                    type="number"
                                    onChange={onFilterEventsDataChange}
                                />                                                    
                            </Form.Group>
                        </Row>
                        <Row>
                            <Form.Group as={Col}>
                                <Form.Label>Cost</Form.Label>
                                <Form.Control
                                    name="cost"
                                    placeholder="Cost"
                                    type="number"
                                    onChange={onFilterEventsDataChange}
                                />                                                    
                            </Form.Group>
                            <Form.Group as={Col}>
                                <Form.Label>Media</Form.Label>
                                <Form.Control
                                    name="media"
                                    placeholder="Media"
                                    onChange={onFilterEventsDataChange}
                                />                                                    
                            </Form.Group>
                        </Row>
                        <Row>
                            <Form.Group as={Col}>
                                <Form.Label>Host</Form.Label>
                                <Form.Control
                                    name="host"
                                    placeholder="Host"
                                    type="number"
                                    onChange={onFilterEventsDataChange}
                                />                                                    
                            </Form.Group>                                                
                            <Form.Group as={Col}>
                                <Form.Label>Categories</Form.Label>
                                <Select
                                    isMulti
                                    options={categories}
                                    onChange={categories => setFilterEventsData({ 
                                        ...filterEventsData,
                                        categories: categories.map(category => category.value)
                                    })}
                                />
                            </Form.Group>
                        </Row>
                        <Button type="submit" variant="primary">Filter Events</Button>
                    </Form>
                </Accordion.Body>
            </Accordion.Item>
        </Accordion>
    );

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
                                {/* Filter Events */}
                                {renderFilterEventsSection()}
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