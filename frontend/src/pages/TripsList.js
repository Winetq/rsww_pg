import React, { useEffect, useState } from "react";

import TripsListElement from "../components/TripsListElement";
import TripsListElementSkeleton from "../components/TripsListElementSkeleton";
import useFetch from "../hooks/useFetch";
import UrlBuilder from "../components/UrlBuilder";
import InfoToast from "../components/InfoToast";
import NotificationToast from "../components/NotificationToast";
import { ToastContainer } from "react-bootstrap";


const exampleTrips = {
    trips: [
        {
            "id": 1,
            "hotel": {
                "id": 1,
                "name": "Samira Club Spa & Aquapark",
                "stars": 3,
                "place": "Kraj / Miasto", // concat kraju i miasta
                "photo": "https://r.cdn.redgalaxy.com/scale/o2/TUI/hotels/NBE16043/S22/19905149.jpg?dstw=1200&dsth=644.0795159896282&srcw=1157&srch=621&srcx=1%2F2&srcy=1%2F2&srcmode=3&type=1&quality=80",

            },
            "tripPrice": 1800, // ca
            "dateStart": "26.06.2022 16:59",
            "dateEnd": "28.06.2022 16:59"
        },
        {
            "id": 2,
            "hotel": {
                "id": 1,
                "name": "Hotel Ładny & Plaża",
                "stars": 3,
                "place": "Kraj / Miasto", // concat kraju i miasta
                "photo": "https://r.cdn.redgalaxy.com/scale/o2/TUI/hotels/AYT13043/S22/20150564.jpg?dstw=1200&dsth=644.0795159896282&srcw=1157&srch=621&srcx=1%2F2&srcy=1%2F2&srcmode=3&type=1&quality=80",

            },
            "tripPrice": 10400, // ca
            "dateStart": "22.03.2022 16:59",
            "dateEnd": "30.03.2022 16:59"
        }
    ]
}


const TripsList = () => {
    const urlBuilder = new UrlBuilder();
    const urlParams = new URLSearchParams(window.location.search);
    const url = new URL(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_URL'));

    for (const key of urlParams.keys())
        url.searchParams.append(key, urlParams.get(key));

    let {data, isPending, error} = useFetch(url.href);
    // data = exampleTrips.trips;
    
    let [notifications, setNotifications] = useState(() => []);
    const notifyDelay = Number.parseInt(process.env['REACT_APP_NOTIFICATIONS_DELAY_MS']);;
    useEffect(() => {
        let ready = true;

        const notifyInterval = setInterval(async () => {
            if(!ready)
                return;
            
            setNotifications([]);
            ready = false;

            try{
                const notify = await fetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_DESTINATION_NOTIFICATIONS')+'?destination='+urlParams.get('destination'));
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

    const skeletonsArray = Array.from(Array(4).keys());

    return (
        <div className="trips-list">
            <div className="fw-bold fs-3 m-4">
                List of trips with selected filters
            </div>

            { isPending ?
                skeletonsArray.map((key) => (
                    <TripsListElementSkeleton key={key}/>
                ))
            :
            error ?
                <InfoToast variant="danger" content={"Loading list of trips failed :( \"" + error + "\""} />
            :
                data.map(trip => (
                    <TripsListElement trip={trip} key={trip.id} urlParams={url.searchParams.toString()}/>
                ))
            }

            <ToastContainer className="m-3 position-fixed" position="top-end" style={{zIndex: 69}}>
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

export default TripsList;