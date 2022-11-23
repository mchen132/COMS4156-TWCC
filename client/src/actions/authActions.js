import axios from 'axios';

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
        const res = await axios.post('http://localhost:8080/user/login',
            loginBody
        );

        if (res.status == 200) {
            navigate('/events');
        }
    } catch (err) {
        console.error(err);
    }
};