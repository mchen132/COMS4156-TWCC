import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './components/layout/Landing';
import Register from './components/auth/Register';
import Login from './components/auth/Login';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <div className="App">
		<Router>
			<>
			<Routes>
				<Route exact path='/' element={<Landing />} />            
				<Route path='/register' element={<Register />} />
				<Route path='/login' element={<Login />} />
			</Routes>
			</>
		</Router>
    </div>
  );
}

export default App;
