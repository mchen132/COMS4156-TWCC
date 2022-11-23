import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import { loginUser } from '../../actions/authActions';

const Register = () => {
    const navigate = useNavigate();
    const [loginData, setLoginData] = useState({
        username: '',
        password: ''
    });

    const { username, password } = loginData;

    const onLogin = (e) => {
        e.preventDefault();
        
        console.log(loginData);
        loginUser(loginData, navigate);
    };

    const onChange = (e) => {
        setLoginData({ ...loginData, [e.target.name]: e.target.value });
    };

    return (
        <>
            <h1>Login</h1>
            <form className="form" onSubmit={e => onLogin(e)}>
                <div className="form-group">
                    <input
                        type="text"
                        placeholder="Username"
                        name="username"
                        value={username}
                        onChange={e => onChange(e)}
                    />
                </div>
                <div className="form-group">
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        value={password}
                        onChange={e => onChange(e)}
                    />
                </div>
                <Button type="submit" variant="primary">Login</Button>
            </form>
        </>
    );
};

export default Register;