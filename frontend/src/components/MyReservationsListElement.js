import React, { useContext, useState } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import ListGroup from "react-bootstrap/ListGroup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { 
    faLocationDot, faCalendar, faHotel, faCoins, faStar, 
    faCircleNotch, faCreditCard, faXmark
} from "@fortawesome/free-solid-svg-icons";
import UrlBuilder from "./UrlBuilder";
import InfoToast from "../components/InfoToast";
import AuthContext from "../context/AuthContext";


const MyReservationsListElement = ({reservation}) => {

    const urlBuilder = new UrlBuilder();
    
    const {user} = useContext(AuthContext);

    let [isPaying, setIsPaying] = useState(false);
    let [isCancelling, setIsCancelling] = useState(false);

    let [isCancellingFailed, setIsCancellingFailed] = useState(false);
    let [isCancellingSucceeded, setIsCancellingSucceeded] = useState(false);
    let [isPaymentFailed, setIsPaymentFailed] = useState(false);
    let [isPaymentSucceeded, setIsPaymentSucceeded] = useState(false);
    const toggleIsCancellingFailed = () => setIsCancellingFailed(!isCancellingFailed);
    const toggleIsCancellingSucceeded = () => setIsCancellingSucceeded(!isCancellingSucceeded);
    const toggleIsPaymentFailed = () => setIsPaymentFailed(!isPaymentFailed);
    const toggleIsPaymentSucceeded = () => setIsPaymentSucceeded(!isPaymentSucceeded);

    const handleCancelReservationClick = (event) => {
        event.preventDefault();
        setIsCancelling(true);

        let request = new XMLHttpRequest();
        request.open('POST', urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL')+reservation.id+'/cancel'+`?user=${user.user_id}`, true);
        request.addEventListener('load', (event) => {
            setIsCancelling(false);
            if(event.currentTarget.statusCode !== 200)
                setIsCancellingFailed(true);
            else 
                setIsCancellingSucceeded(true);
            setTimeout(() => window.location.reload(true), 3000);
        });
        request.send();
    };

    const handlePayForReservationClick = (event) => {
        event.preventDefault();
        setIsPaying(true);

        let request = new XMLHttpRequest();
        request.open('POST', urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL')+"/"+reservation.id+'/payment'+`?user=${user.user_id}`, true);
        request.addEventListener('load', (event) => {
            setIsPaying(false);

            if(event.currentTarget.status !== 202)
                setIsPaymentFailed(true);
            else 
                setIsPaymentSucceeded(true);
        });
        request.send();
    };

    return (
        <div>
        <Card className="w-75 mx-auto p-3 shadow mb-3">
            <div className="row g-0">
                <div className="col-md-6 my-auto">
                    <Card.Img src={reservation.hotel.photo} alt="Hotel Image" />
                </div>
                <div className="col-md-6">
                    <Card.Body>
                        <Card.Title className="border-bottom">
                            <h5 className="text-success">
                                <FontAwesomeIcon icon={faLocationDot} className="me-1 fa-fw"/>
                                {reservation.hotel.place}
                            </h5>
                        </Card.Title>
                        <ListGroup className="list-group-flush">
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faCalendar} className="me-1 fa-fw" />
                                {reservation.dateStart} - {reservation.dateEnd}
                            </ListGroup.Item>
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faHotel} className="me-1 fa-fw" />
                                <span className="me-2">{reservation.hotel.name}</span>
                                {reservation.hotel.stars} <FontAwesomeIcon icon={faStar}/>
                            </ListGroup.Item>
                            <ListGroup.Item className="px-1">
                                <FontAwesomeIcon icon={faCoins} className="me-1 fa-fw" />
                                {reservation.tripPrice} zł
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
                                <Button 
                                    variant="outline-success" 
                                    className={"w-100 " + (isPaymentSucceeded ? "disabled " : null)} 
                                    onClick={handlePayForReservationClick}
                                >
                                    <FontAwesomeIcon icon={faCreditCard} className="fa-fw me-1" />
                                    {isPaymentSucceeded ? "Paid" : "Pay for trip" }
                                </Button>
                            }
                        </div>
                        {/* <div> 
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
                        </div> */}
                    </Card.Body>
                </div>
            </div>
        </Card>
        {/* {isCancellingFailed ? <InfoToast variant="danger" content={"Cancelling reservation failed"} onClose={toggleIsCancellingFailed} /> : null} */}
        {/* {isCancellingSucceeded ? <InfoToast variant="success" content={"Cancelling reservation succeeded"} onClose={toggleIsCancellingSucceeded} /> : null} */}
        {isPaymentFailed ? <InfoToast variant="danger" content={"Payment for reservation failed"} onClose={toggleIsPaymentFailed} /> : null}
        {isPaymentSucceeded ? <InfoToast variant="success" content={"Payment for reservation succeeded"} /> : null}
        </div>
    )
}

export default MyReservationsListElement;