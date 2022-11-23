import axios from 'axios';
import { setAuthToken } from '../utils/authUtil';

export const registerUser = async (registerBody, navigate) => {
    try {
        const res = await axios.post('http://localhost:8080/user/register',
            registerBody,
            { headers: { 'Content-Type': 'application/json' }}
        );

        if (res.status == 200) {
            navigate('/login');
            return res.data;
        }
    } catch (err) {
        console.error(err);
    }
};

export const loginUser = async (loginBody, navigate) => {
    try {
        const res = await axios.post('http://localhost:8080/user/login', loginBody);

        if (res.status == 200) {
            const { email, id, token, username } = res.data;
            
            localStorage.setItem('userEmail', email);
            localStorage.setItem('userId', id);
            localStorage.setItem('username', username);
            localStorage.setItem('jwtInfo', token);

            setAuthToken(token);
            
            navigate('/events');
        }
    } catch (err) {
        console.error(err);
    }
};