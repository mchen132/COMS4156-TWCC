import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './components/layout/Landing';
import Register from './components/auth/Register'
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <div className="App">
		<Router>
			<>
			<Routes>
				<Route path='/register' element={<Register />} />
				<Route exact path='/' element={<Landing />} />            
			</Routes>
			</>
		</Router>
    </div>
  );
}

export default App;
