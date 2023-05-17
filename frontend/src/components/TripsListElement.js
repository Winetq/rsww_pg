import React from "react";
import { Link } from "react-router-dom";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLocationDot, faCalendar, faHotel, faCoins } from "@fortawesome/free-solid-svg-icons";


export default function TripsListElement({trip}) {

    return (
        <Card className="w-75 mx-auto p-3 shadow mb-3">
            <Link to={`/trips/${trip.id}/`} className="stretched-link"/>
            <div className="row g-0">
                <div className="col-md-5">
                    <Card.Img src={trip.img} alt="Hotel Image" />
                </div>
                <div className="col-md-7">
                    <Card.Body>
                        <Card.Title className="border-bottom">
                            <h5 className="text-success">
                                <FontAwesomeIcon icon={faLocationDot} className="me-1 fa-fw"/>
                                {trip.location}, {trip.days} days
                            </h5>
                        </Card.Title>
                        <Card.Text>
                            <ListGroup className="list-group-flush">
                                <ListGroup.Item className="px-1">
                                    <FontAwesomeIcon icon={faCalendar} className="me-1 fa-fw" />
                                    {trip.startDate} - {trip.endDate}
                                </ListGroup.Item>
                                <ListGroup.Item className="px-1">
                                    <FontAwesomeIcon icon={faHotel} className="me-1 fa-fw" />
                                    {trip.hotel}
                                </ListGroup.Item>
                                <ListGroup.Item className="px-1">
                                    <FontAwesomeIcon icon={faCoins} className="me-1 fa-fw" />
                                    {trip.price} zł
                                </ListGroup.Item>
                            </ListGroup>
                        </Card.Text>
                    </Card.Body>
                </div>
            </div>
        </Card>
    )
}