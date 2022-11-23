import axios from 'axios';

export const setAuthToken = token => {
    if (token) {
        axios.defaults.headers.common['Authorization'] = token;
    } else {
        delete axios.defaults.headers.common['Authorization'];
    }
};

export const getAuthInformation = key => {
    if (localStorage.getItem(key)) {
        return localStorage.getItem(key);
    } else {
        console.error(`getAuthInformation(): Key does not exist (${key})`);
    }
}