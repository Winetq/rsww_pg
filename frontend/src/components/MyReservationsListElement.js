import React, { useState } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import ListGroup from "react-bootstrap/ListGroup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { 
    faLocationDot, faCalendar, faHotel, faCoins, faStar, 
    faCircleNotch, faCreditCard, faXmark
} from "@fortawesome/free-solid-svg-icons";


const MyReservationsListElement = ({reservation}) => {

    let [isPaying, setIsPaying] = useState(false);
    let [isCancelling, setIsCancelling] = useState(false);

    const handleCancelReservationClick = (event) => {
        event.preventDefault();

        setIsCancelling(true);

        let reservations = JSON.parse(sessionStorage.getItem("reservations"));
        reservations = reservations.filter((r) => {
            return r.tripId !== reservation.tripId;
        });
        sessionStorage.setItem("reservations", JSON.stringify(reservations));

        setIsCancelling(false);
        alert('Reservation cancelled');
        window.location.reload(true);
    };

    const handlePayForReservationClick = (event) => {
        setIsPaying(true);
        event.preventDefault();
        setIsPaying(false);
    };

    return (
        <Card className="w-75 mx-auto p-3 shadow mb-3">
            <div className="row g-0">
                <div className="col-md-6 my-auto">
                    <Card.Img src={reservation.trip.hotel.photo} alt="Hotel Image" />
                </div>
                <div className="col-md-6">
                    <Card.Body>
                        <Card.Title className="border-bottom">
                            <h5 className="text-success">
                                <FontAwesomeIcon icon={faLocationDot} className="me-1 fa-fw"/>
                                {reservation.trip.hotel.country} | {reservation.trip.hotel.city}
                            </h5>
                        </Card.Title>
                        <ListGroup className="list-group-flush">
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faCalendar} className="me-1 fa-fw" />
                                {reservation.trip.transport.departureDateTime}
                            </ListGroup.Item>
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faHotel} className="me-1 fa-fw" />
                                <span className="me-2">{reservation.trip.hotel.name}</span>
                                {reservation.trip.hotel.stars} <FontAwesomeIcon icon={faStar}/>
                            </ListGroup.Item>
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faCoins} className="me-1 fa-fw" />
                                {reservation.trip.tripPrice} zł
                            </ListGroup.Item>
                        </ListGroup>
                        <div className="mb-2">
                            {
                            isPaying ? 
                                <Button type="submit" variant="outline-success" className="w-100 disabled">
                                    <FontAwesomeIcon icon={faCircleNotch} className="fa-fw me-1 fa-spin" />
                                    Processing....
                                </Button>
                            :
                                <Button variant="outline-success" className="w-100" onClick={handlePayForReservationClick}>
                                    <FontAwesomeIcon icon={faCreditCard} className="fa-fw me-1" />
                                    Pay for trip
                                </Button>
                            }
                        </div>
                        <div> 
                            {
                            isCancelling ?
                                <Button type="submit" variant="outline-danger" className="w-100 disabled">
                                    <FontAwesomeIcon icon={faCircleNotch} className="fa-fw me-1 fa-spin" />
                                    Processing....
                                </Button>
                            :
                                <Button variant="outline-danger" className="w-100" onClick={handleCancelReservationClick}>
                                    <FontAwesomeIcon icon={faXmark} className="fa-fw me-1" />
                                    Cancel reservation
                                </Button>
                            } 
                        </div>
                    </Card.Body>
                </div>
            </div>
        </Card>
    )
}
export default MyReservationsListElement;