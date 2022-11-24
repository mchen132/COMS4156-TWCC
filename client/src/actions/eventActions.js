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
    }
};