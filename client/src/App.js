import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './components/Layout/Landing';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <div className="App">
      <Router>
        <>
          <Routes>
            <Route exact path='/' element={<Landing/>} />
          </Routes>
        </>
      </Router>
    </div>
  );
}

export default App;
