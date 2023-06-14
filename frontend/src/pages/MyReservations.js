import { useContext } from "react";
import InfoToast from "../components/InfoToast";
import MyReservationsListElement from "../components/MyReservationsListElement";
import TripsListElementSkeleton from "../components/TripsListElementSkeleton";
import UrlBuilder from "../components/UrlBuilder";
import useFetch from "../hooks/useFetch";
import AuthContext from "../context/AuthContext";


const MyReservations = () => {

    const {user} = useContext(AuthContext);

    const urlBuilder = new UrlBuilder();
    let {data, isPending, error} = useFetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_MYTRIPS_URL')+`?user=${user.user_id}`);

    return (
        isPending ? 
            <TripsListElementSkeleton />
        :
        error ?
            <InfoToast variant="danger" content={"Loading details of trip has failed :( \"" + error + "\""} />
        :
        data.length > 0 ?
        
            <div className="my-reservations my-3">
                {data.map((reservation) => (
                    <MyReservationsListElement reservation={reservation} key={reservation.id} />
                ))}
            </div>
        :
        
            <div className="text-center fw-bold my-4 fs-3">
                You don't have any reservations
            </div>            
    )
}

export default MyReservations;