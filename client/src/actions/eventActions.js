import axios from 'axios';
import { clearAuthLocalStorageInfo } from '../utils/authUtil';

export const getEvents = async () => {
    try {
        const res = await axios.get("http://localhost:8080/events");
        
        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};

/**
 * Gets a list of filtered events based on the specified
 * query parameters.
 * 
 * @param {Object} filterEventsQueryParams 
 * @returns events | error
 */
export const filterEvents = async filterEventsQueryParams => {
    try {
        let filterEventsQueryString = Object.keys(filterEventsQueryParams).length > 0
            ? '?'
            : '';
        for (const queryParam in filterEventsQueryParams) {
            const queryValue = filterEventsQueryParams[queryParam];
            if (filterEventsQueryString.includes('=')) {
                filterEventsQueryString += `&${queryParam}=${queryValue}`
            } else {
                filterEventsQueryString += `${queryParam}=${queryValue}`
            }
        }

        const res = await axios.get(`http://localhost:8080/filterEvents${filterEventsQueryString}`);

        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};

export const createEvent = async (newEvent) => {
    try {
        const res = await axios.post("http://localhost:8080/events", newEvent);

        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};

export const updateEvent = async (updateEvent) => {
    try {
        const res = await axios.put("http://localhost:8080/events", updateEvent);

        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};

export const deleteEvent = async (eventId) => {
    try {
        const res = await axios.delete(`http://localhost:8080/events/${eventId}`);

        if (res.status === 200) {
            return eventId;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};

export const getEventStatistics = async () => {
    try {
        const res = await axios.get("http://localhost:8080/events/statistics");

        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        if (err.response && err.response.status === 401) {
            clearAuthLocalStorageInfo();
        }

        console.error(err);
        throw err;
    }
};