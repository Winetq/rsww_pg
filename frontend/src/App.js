import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Container from 'react-bootstrap/Container';

import CollapsibleNavbar from './components/Navbar';
import Footer from './components/Footer';

import Home from './pages/Home';
import Login from './pages/Login';
import Logout from './pages/Logout';
import TripsList from './pages/TripsList';
import TripDetails from './pages/TripDetails';


export default function App() {
  return (
    <Router>
      <CollapsibleNavbar />
      <Container fluid="xl">
      <Routes>
        <Route exact path="/login" element={<Login />} />
        <Route exact path="/logout" element={<Logout />} />
        <Route exact path="/trips" element={<TripsList />} />
        <Route exact path="/trips/:id" element={<TripDetails />} />
        <Route exact path="/" element={<Home />} />
      </Routes>
      </Container>
      <Footer />
    </Router>
  );
}