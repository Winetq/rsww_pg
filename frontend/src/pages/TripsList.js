import React from "react";

import TripsListElement from "../components/TripsListElement";
import TripsListElementSkeleton from "../components/TripsListElementSkeleton";


export default class TripsList extends React.Component {
    
    render() {
        const trips = [
            {
                "id": 1,
                "img": "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                "hotel": "Beautifull Hotel",
                "location": "Egipt",
                "startDate": "2023-01-01",
                "endDate": "2023-01-08",
                "days": 7,
                "price": 1200
            },
            {
                "id": 2,
                "img": "https://theluxurytravelexpert.com/wp-content/uploads/2021/06/best-hotels-in-France.jpg",
                "hotel": "Beach Hotel",
                "location": "France",
                "startDate": "2023-02-01",
                "endDate": "2023-02-15",
                "days": 14,
                "price": 16000
            }
        ]

        return (
            <div className="trips-list">
                <div className="fw-bold fs-3 m-4">
                    List of trips
                </div>

                <TripsListElementSkeleton />

                {trips.map(trip => (
                    <TripsListElement trip={trip} key={trip.id}/>
                ))}
            </div>
        )
    }
}