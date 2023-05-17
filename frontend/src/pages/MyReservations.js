import MyReservationsListElement from "../components/MyReservationsListElement";


const MyReservations = () => {
    let reservations = sessionStorage.getItem('reservations');
    if(reservations != null)
        reservations = JSON.parse(reservations);

    return (
        !reservations || reservations.length <= 0 ? 
        <div className="text-center fw-bold my-4 fs-3">
            You don't have any reservations
        </div>
        :
        <div className="my-reservations my-3">
            {reservations.map((reservation) => (
                <MyReservationsListElement reservation={reservation} key={reservation.tripId} />
            ))}
        </div>
    )
}

export default MyReservations;