import './App.css';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import Landing from './components/layout/Landing';
import Register from './components/auth/Register';
import Login from './components/auth/Login';
import EventsContainer from './components/events/EventsContainer';
import 'bootstrap/dist/css/bootstrap.min.css';
import { clearAuthLocalStorageInfo, setAuthToken, getAuthInformation } from './utils/authUtil';
import CustomNavbar from './components/layout/CustomNavbar';
import EventStatistics from './components/statistics/EventStatistics';

if (localStorage.token) {
	setAuthToken(localStorage.token);
}

const App = () => {
	const navigate = useNavigate();
	const onLogout = () => {
		clearAuthLocalStorageInfo();
		navigate('/');
	};

	return (
		<div className="App">
			<CustomNavbar
				home="/events"
				title="TWCC Events"
				leftLinks={[{ href: 'events', name: 'Events' }, { href: 'events/statistics', name: 'Event Statistics' }]}
				rightLinks={getAuthInformation('token') ? [{ name: 'Logout', onClick: onLogout }] : []}
			/>
			<Routes>
				<Route exact path='/' element={<Landing />} />            
				<Route path='/register' element={<Register />} />
				<Route path='/login' element={<Login />} />
				<Route path='/events' element={<EventsContainer />} />
				<Route path='/events/statistics' element={<EventStatistics />} />
			</Routes>
		</div>
	);
};

export default App;
