import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './components/layout/Landing';
import Register from './components/auth/Register';
import Login from './components/auth/Login';
import EventsContainer from './components/events/EventsContainer';
import 'bootstrap/dist/css/bootstrap.min.css';
import { setAuthToken } from './utils/authUtil';
import CustomNavbar from './components/layout/CustomNavbar';
import EventStatistics from './components/statistics/EventStatistics';

if (localStorage.token) {
	setAuthToken(localStorage.token);
}

const App = () => {
	return (
		<div className="App">
			<CustomNavbar home="/events" title="TWCC Events" links={[{ href: 'events', name: 'Events' }, { href: 'events/statistics', name: 'Event Statistics' }]} />
			<Router>
				<>
					<Routes>
						<Route exact path='/' element={<Landing />} />            
						<Route path='/register' element={<Register />} />
						<Route path='/login' element={<Login />} />
						<Route path='/events' element={<EventsContainer />} />
						<Route path='/events/statistics' element={<EventStatistics />} />
					</Routes>
				</>
			</Router>
		</div>
	);
};

export default App;
