import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import { registerUser } from '../../actions/authActions';

const Register = () => {
    const navigate = useNavigate();
    const [registerData, setRegisterData] = useState({
        firstName: '',
        lastName: '',
        age: null,
        email: '',
        username: '',
        password: ''
    });

    const { firstName, lastName, age, email, username, password } = registerData;

    const onRegister = (e) => {
        e.preventDefault();
        
        console.log(registerData);
        registerUser(registerData, navigate);
    };

    const onChange = (e) => {
        setRegisterData({ ...registerData, [e.target.name]: e.target.value });
    };

    return (
        <>
            <h1>Sign Up</h1>
            <p className="lead">Create Your Account</p>
            <form className="form" onSubmit={e => onRegister(e)}>
                <div className="form-group">
                    <input
                        type="text"
                        placeholder="First Name"
                        name="firstName"
                        value={firstName}
                        onChange={e => onChange(e)}
                    />
                </div>
                <div className="form-group">
                    <input
                        type="text"
                        placeholder="Last Name"
                        name="lastName"
                        value={lastName}
                        onChange={e => onChange(e)}
                    />
                </div>
                <div className="form-group">
                    <input
                        type="text"
                        placeholder="Age"
                        name="age"
                        value={age}
                        onChange={e => onChange(e)}
                    />
                </div>
                <div className="form-group">
                    <input
                        type="email"
                        placeholder="Email Address"
                        name="email"
                        value={email}
                        onChange={e => onChange(e)}
                    />
                </div>
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
                <Button type="submit" variant="primary">Register</Button>
            </form>
            <p className="my-1">
                Already have an account? <Link to="/login">Sign In</Link>
            </p>
        </>
    );
};

export default Register;