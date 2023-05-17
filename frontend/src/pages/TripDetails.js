import React from "react";
import { useParams } from "react-router-dom";



const TripDetails = () => {
    const { id } = useParams();

    return (
        <div className="fw-bold fs-3 m-4">
            This page will be with details of trip with id = { id } 
        </div>
    )
}

export default TripDetails;