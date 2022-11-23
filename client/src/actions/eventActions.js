import axios from 'axios';

export const getEvents = async () => {
    try {
        const res = await axios.get("http://localhost:8080/events");
        console.log(res);
    } catch (err) {
        console.error(err);
    }
};