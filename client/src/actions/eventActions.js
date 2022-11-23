import axios from 'axios';

export const getEvents = async () => {
    try {
        const res = await axios.get("http://localhost:8080/events");
        
        if (res.status === 200) {
            return res.data;
        }
    } catch (err) {
        console.error(err);
    }
};