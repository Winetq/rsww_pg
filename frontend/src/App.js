import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import PrivateRoute from './components/PrivateRoute';
import { AuthProvider } from '../src/context/AuthContext';

import Container from 'react-bootstrap/Container';
import CollapsibleNavbar from './components/Navbar';
import Footer from './components/Footer';

import Home from './pages/Home';
import Login from './pages/Login';
import Logout from './pages/Logout';
import TripsList from './pages/TripsList';
import TripDetails from './pages/TripDetails';
import PrivateComponent from './components/PrivateComponent';
import MyReservations from './pages/MyReservations';


export default function App() {
    return (
    <Router>
        <AuthProvider>
        <PrivateComponent>
            <CollapsibleNavbar />
        </PrivateComponent>

        <Container fluid="xl">
        <Routes>
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/logout" element={<Logout />} />
            <Route exact path='/trips' element={<PrivateRoute />}>
                <Route exact path="/trips" element={<TripsList />} />
            </Route>
            <Route exact path='/trips/:id' element={<PrivateRoute />}>
                <Route exact path="/trips/:id" element={<TripDetails />} />
            </Route>
            <Route exact path='/my-reservations' element={<PrivateRoute />}>
                <Route exact path="/my-reservations" element={<MyReservations />} />
            </Route>
            <Route exact path='/' element={<PrivateRoute />}>
            <Route exact path="/" element={<Home />} />
            </Route>
        </Routes>
        </Container>

        <PrivateComponent>
            <Footer />
        </PrivateComponent>
        </AuthProvider>
    </Router>
  );
}