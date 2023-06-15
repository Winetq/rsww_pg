import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import useFetch from "../hooks/useFetch";
import UrlBuilder from "../components/UrlBuilder";
import TripsListElementSkeleton from "../components/TripsListElementSkeleton";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { 
    faStar, faHotel, faLocationDot, faTruckPlane, faPlaneDeparture, 
    faPlaneArrival, faClock, faSignature, faRightLong, faCircleInfo,
    faUtensils, faBed, faCircleNotch
} from "@fortawesome/free-solid-svg-icons";
import { Button, ToastContainer } from "react-bootstrap";
import TripRoomsElement from "../components/TripRoomsElement";
import TripFoodElement from "../components/TripFoodElement";
import InfoToast from "../components/InfoToast";
import AuthContext from "../context/AuthContext";
import NotificationToast from "../components/NotificationToast";


const trip = {
    hotel: {
        "id": 1,
        "name": "Nazwa hotelu",
        "country": "Kraj hotelu",
        "city": "Miasto hotelu", // may be null/empty
        "stars": 3, // integer 0 - 5, 0 jesli nie ma danych
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vehicula tellus vel porta accumsan. Etiam nec eleifend tellus, vel facilisis lorem. Aenean interdum sollicitudin risus a blandit. \
                        Vivamus dignissim orci a justo sodales, ut fringilla nunc porttitor. Fusce dignissim pellentesque vestibulum. Suspendisse eu orci cursus, bibendum leo elementum, mollis magna. Vivamus vitae pretium nunc, finibus \
                        malesuada lacus. Donec vitae justo auctor, malesuada neque vitae, hendrerit ante. Aenean at tincidunt sem. Ut laoreet egestas est vel suscipit. Sed laoreet massa sit amet tempor finibus. Sed maximus eros quis commodo \
                        ultrices. In vel mi eros. Phasellus pretium turpis in nisl consectetur, ut tristique enim luctus. Aliquam erat volutpat. Vestibulum tincidunt ex in dui auctor fringilla. Nunc facilisis purus et lacinia viverra. \
                        Donec rhoncus nisl in augue facilisis, eget venenatis augue ullamcorper. Phasellus vitae lorem sed elit vestibulum consequat quis et neque. Cras accumsan sed leo sed volutpat. Etiam ut posuere nulla. Pellentesque \
                        et augue elementum, feugiat dui sit amet, consectetur mauris. Praesent cursus nunc eget aliquet tristique. Mauris ultrices congue vulputate. Quisque viverra purus justo, at dictum sem pulvinar efficitur. \
                        Mauris dignissim accumsan elementum. Curabitur aliquam sodales urna at interdum. Duis orci dolor, posuere egestas nisl eget, consectetur placerat ipsum. Donec vel aliquet nibh. Etiam non elit at eros vehicula \
                        vulputate. Curabitur ullamcorper, augue id ullamcorper cursus, enim velit vulputate mi, vel maximus eros odio vel orci. Cras neque orci, eleifend et est ut, congue rutrum urna. Phasellus eu erat nec tortor \
                        sollicitudin sagittis. Maecenas iaculis nulla nibh, at dignissim lacus finibus eu. In consectetur pharetra vulputate. Suspendisse sit amet interdum tellus. Etiam eget tellus eget justo fermentum dictum eu et \
                        augue. Nam sed sodales dui. Nullam in bibendum enim. Praesent sit amet est a urna blandit sagittis vitae vel arcu. Vestibulum sed risus consectetur, auctor dolor lacinia, consectetur ipsum. Aenean id massa \
                        volutpat, tempor augue et, cursus tortor. Nulla facilisi. Sed finibus nibh at sollicitudin semper. Fusce eleifend sed massa id vestibulum. Donec sed tempor est. Suspendisse ac risus ac lacus volutpat fringilla. \
                        Duis vulputate consectetur tortor vitae iaculis. Fusce consequat vel felis eget varius. Nunc dictum odio ligula. Quisque facilisis erat in semper hendrerit. Nam fringilla nisi non euismod viverra. Fusce gravida \
                        sagittis accumsan. Suspendisse quis consequat nibh. Praesent vestibulum est ac metus bibendum pretium id a ex. Vivamus euismod nec justo efficitur tempor. Ut sollicitudin lobortis egestas.",
        "photo": "https://r.cdn.redgalaxy.com/scale/o2/TUI/hotels/NBE16043/S22/19905149.jpg?dstw=1200&dsth=644.0795159896282&srcw=1157&srch=621&srcx=1%2F2&srcy=1%2F2&srcmode=3&type=1&quality=80",
        "rooms": [ // dostępne pokoje, wedlug typu
            {
                "capacity": 4, // max liczba osob w pokoju
                "name": "Pokój 4-osobowy", // nazwa widoczna na karcie
                "features": "klimatyzacja|TV|telefon" // lista zapisana w formacie string oddzielona |
            },
            {
                "capacity": 6, // max liczba osob w pokoju
                "name": "Pokój 6-osobowy", // nazwa widoczna na karcie
                "features": "klimatyzacja|TV|telefon|suszarka|lodówka" // lista zapisana w formacie string oddzielona |
            }
        ],
        "airport": "Paris [CDG]",
        "food": "none|breakfast|two-dishes|three-dishes|all-inclusive" // enum, minimum jedna z tych opcji, może nie być wcale
    },
    transport: {
        "departureAirport": "Warsaw (KOD)",
        "arrivalAirport": "Paris (CDG)",
        "departureDateTime": "26.06.2022 16:59",
        "arrivalDate": "26.06.2022 18:00", // format dd.mm.yyyy hh:MM
        "travelTime": 65, // podane w minutach,
        "placesCount": 50, // liczba miejsc w samolocie
        "placesOccupied": 15 // liczba zajetych miejsc
    },
    returnTransport: {
        "arrivalAirport": "Warsaw (KOD)",
        "departureAirport": "Paris (CDG)",
        "departureDateTime": "30.06.2022 16:59",
        "arrivalDate": "30.06.2022 18:00", // format dd.mm.yyyy hh:MM
        "travelTime": 65, // podane w minutach,
        "placesCount": 50, // liczba miejsc w samolocie
        "placesOccupied": 15 // liczba zajetych miejsc
    }
}


const TripDetails = () => {
    const checkReservation = () => {
        const reservations = sessionStorage.getItem('reservations');
        if(reservations != null) {
            for(let reservation of JSON.parse(reservations))
                if(reservation.tripId == id)
                    return true;
        }
        return false;
    }

    const { id } = useParams();

    const {user} = useContext(AuthContext);
    let [room, setRoom] = useState(null);
    let [food, setFood] = useState(null);
    let [reserved, setReserved] = useState(false);
    let [isReserving, setIsReserving] = useState(false);

    let [isSelectingFailed, setIsSelectingFailed] = useState(false);
    let [isReservationSucceeded, setIsReservationSucceeded] = useState(false);
    let [isReservationFailed, setIsReservationFailed] = useState(false);
    const toggleIsSelectingFailed = () => setIsSelectingFailed(!isSelectingFailed);
    const toggleIsReservationSucceeded = () => setIsReservationSucceeded(!isReservationSucceeded);
    const toggleIsReservationFailed = () => setIsReservationFailed(!isReservationFailed);

    const urlBuilder = new UrlBuilder();
    let {data, isPending, error} = useFetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL') + "/" + id);
    //  data = trip;
    
    let handleClickReserveTrip = async (event) => {
        event.preventDefault();
        
        if(room == null || food == null) {
            setIsSelectingFailed(true);
            return;
        }
        setIsReserving(true);

        let request = new XMLHttpRequest();
        request.open('POST', urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL')+"/"+id+'/reserve', true);
        request.addEventListener('load', (event) => {
            if(event.currentTarget.status !== 202){
                setIsReservationFailed(true);
            } else {
                // let reservations = sessionStorage.getItem('reservations');
                // if(reservations == null)
                //     reservations = [];
                // else
                //     reservations = JSON.parse(reservations);
                
                // reservations.push({
                //     tripId: id,
                //     trip: data,
                //     room: room,
                //     food: food
                // });
                // sessionStorage.setItem('reservations', JSON.stringify(reservations));

                setReserved(true);
                setIsReservationSucceeded(true);
            }
            setIsReserving(false);
        });
        request.send(JSON.stringify({
            "user": user.user_id,
            "tripId": id,
            "food": food,
            "room": room,
        }));
    }

    if(!isPending && !error && data)
        data.hotel.rooms.forEach((room, idx) => {
            data.hotel.rooms[idx].key = idx;
        });

    let [notifications, setNotifications] = useState(() => null);
    const notifyDelay = 2*1000;
    useEffect(() => {
        let ready = true;

        const notifyInterval = setInterval(async () => {
            if(!ready)
                return;

            setNotifications([]);
            ready = false;
            
            try{
                const notify = await fetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL')+`/${id}/notifications`);
                if(notify.status === 200) {
                    setNotifications(await notify.json());
                }
            } catch {
                setNotifications([{notification: 'Could not parse notification :('}]);
            } finally {
                ready = true;
            }
        }, notifyDelay);
        
        return () => clearInterval(notifyInterval);
    }, []);

    return (
        isPending ? 
            <TripsListElementSkeleton className="my-3" /> 
        :
        error ?
            <InfoToast variant="danger" content={"Loading details of trip has failed :( \"" + error + "\""} />
        :

        <div className="tripDetails">
        <div className="my-3 row shadow rounded py-4 px-3 gx-4">
            <div className="col-12 col-lg-9">
                <img className="img-fluid rounded" loading="lazy" src={data.hotel.photo} alt="Hotel Photo" />
            </div>
            <div className="col-12 col-lg-3">
                <div className="text-center border-bottom border-top mb-2 py-2">
                    If you want to be 100% sure you will buy this trip you have to reserve it
                </div>
                <div className="border-bottom pb-2 mb-2">
                {
                    isReserving ? 
                        <Button type="submit" variant="success" className="w-100 disabled">
                            <FontAwesomeIcon icon={faCircleNotch} className="fa-fw me-1 fa-spin" />
                            Processing....
                        </Button>
                    :
                        <Button variant="success w-100" onClick={handleClickReserveTrip} className={reserved ? "disabled" : ""}>
                            <FontAwesomeIcon icon={faSignature} className="fa-fw me-1" />
                            {isReservationSucceeded ?
                                "Reserved"
                            :
                                "Reserve This Trip"
                            }
                        </Button>
                }                    
                </div>
                <div>
                    <div className="text-center fw-bold mb-2">
                        Selected configuration
                    </div>
                    <div className="d-flex justify-content-start mb-2">
                        <FontAwesomeIcon icon={faLocationDot} className="fa-fw me-1 bg-info p-2 rounded text-white" />
                        <div className="my-auto">{data.hotel.country} | {data.hotel.city}</div>
                    </div>
                    <div className="d-flex justify-content-start mb-2">
                        <FontAwesomeIcon icon={faHotel} className="fa-fw me-1 bg-warning p-2 rounded text-white" />
                        <div className="my-auto">{data.hotel.name}</div>
                    </div>
                    <div className="d-flex justify-content-start mb-2">
                        <FontAwesomeIcon icon={faBed} className="fa-fw me-1 bg-success p-2 rounded text-white" />
                        <div className="my-auto">{room ? 
                            <span className="text-success">{room.name}</span> 
                            : 
                            <span className="text-danger">{"Not selected"}</span>}</div>
                    </div>
                    <div className="d-flex justify-content-start mb-2">
                        <FontAwesomeIcon icon={faUtensils} className="fa-fw me-1 bg-success p-2 rounded text-white" />
                        <div className="my-auto">{food ? 
                            <span className="text-success">{food}</span>
                            : 
                            <span className="text-danger">{"Not selected"}</span>}</div>
                    </div>
                </div>
            </div>
        </div>
        <div className="row mb-3 px-3 py-4 gx-4 rounded shadow">
            <div className="mb-2 pb-2 text-warning border-bottom d-flex justify-content-start w-100 align-items-center fw-bold">
                <FontAwesomeIcon icon={faHotel} className="fa-fw me-2 bg-warning p-2 rounded text-white" />
                <div className="me-2">{data.hotel.name}</div>
                <div className="me-2">{data.hotel.stars}<FontAwesomeIcon icon={faStar} className="fa-fw"/></div>
            </div>
            <div className="mb-2 pb-2 text-info border-bottom d-flex justify-content-start w-100 align-items-center fw-bold">
                <FontAwesomeIcon icon={faLocationDot} className="fa-fw me-2 bg-info p-2 rounded text-white" />
                <div className="me-2">{data.hotel.country} | {data.hotel.city}</div>
            </div>
            <div className="mb-2 pb-2 text-success border-bottom d-flex justify-content-start w-100 align-items-top">
                <FontAwesomeIcon icon={faTruckPlane} className="fa-fw me-2 bg-success p-2 rounded text-white my-auto" />
                <div className="me-2">
                    <div><FontAwesomeIcon icon={faPlaneDeparture} className="fa-fw me-1"/> Departure</div>
                    <div>{data.transport.departureAirport}</div>
                    <div>{data.transport.departureDateTime}</div>
                </div>
                <div className="mx-3 my-auto">
                    <FontAwesomeIcon icon={faRightLong} />
                </div>
                <div className="mx-2 text-center">
                    <div><FontAwesomeIcon icon={faClock} className="fa-fw me-1"/>Travel time</div>
                    <div>{data.transport.travelTime} min</div>
                </div>
                <div className="mx-3 my-auto">
                    <FontAwesomeIcon icon={faRightLong} />
                </div>
                <div className="mx-2">
                    <div><FontAwesomeIcon icon={faPlaneArrival} className="fa-fw me-1"/>Arrival</div>
                    <div>{data.transport.arrivalAirport}</div>
                    <div>{data.transport.arrivalDate}</div>
                </div>
            </div>
            <div className="border-bottom pb-2 mb-2">
                <div className="d-flex justify-content-start">
                    <FontAwesomeIcon icon={faCircleInfo} className="fa-fw me-2 bg-success p-2 rounded text-white" />
                    <div className="my-auto text-success">Hotel Details</div>
                </div>
                <div>{data.hotel.description}</div>
            </div>
        </div>
        <div className="row mb-3 px-3 py-4 gx-4 rounded shadow">
            <div className="mb-2 pb-2 border-bottom">
                <div className="mb-2 text-success d-flex justify-content-start fw-bold">
                    <FontAwesomeIcon icon={faUtensils} className="fa-fw me-2 bg-success p-2 rounded text-white" />
                    <div className="my-auto">Food</div>
                </div>
                <div className="d-flex justify-items-start">
                    {data.hotel.food.split("|").map((food) => (
                        <TripFoodElement food={food} key={food} setFood={setFood} />
                    ))}
                </div>
            </div>
            <div className="mb-2">
                <div className="pb-2 mb-2 text-success d-flex justify-content-start fw-bold">
                    <FontAwesomeIcon icon={faBed} className="fa-fw me-2 bg-success p-2 rounded text-white" />
                    <div className="my-auto">Rooms</div>
                </div>
                <div className="row row-cols-1 row-cols-lg-3 gy-2 g-0">
                    {data.hotel.rooms.map((room, idx) => (
                        <TripRoomsElement room={room} setRoom={setRoom} key={idx}/>
                    ))}
                </div>
            </div>
        </div>
        {isReservationFailed ? <InfoToast variant="danger" content={"Reserving trip failed"} onClose={toggleIsReservationFailed} /> : null}
        {isReservationSucceeded ? <InfoToast variant="success" content={"Trip has been reserved successfully"} /> : null}
        {isSelectingFailed ? <InfoToast variant="danger" content={"Please select room and food before making reservation"} onClose={toggleIsSelectingFailed} /> : null}
        <ToastContainer className="m-3" position="top-end" style={{zIndex: 69}}>
        { notifications ? 
            notifications.map((notification, idx) => (
                <NotificationToast variant="warning" content={notification.notification} key={idx} />
            )) 
        : 
            null
        }
        </ToastContainer>
        </div>
    )
}

export default TripDetails;