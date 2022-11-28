import axios from 'axios';

export const setAuthToken = token => {
    if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        delete axios.defaults.headers.common['Authorization'];
        clearAuthLocalStorageInfo();
    }
};

export const getAuthInformation = key => {
    if (localStorage.getItem(key)) {
        return localStorage.getItem(key);
    } else {
        console.error(`getAuthInformation(): Key does not exist (${key})`);
    }
}

// Clear local storage variables
export const clearAuthLocalStorageInfo = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
};