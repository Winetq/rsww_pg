import { faHandPointer, faPeopleGroup } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


const TripRoomsElement = ({room, setRoom}) => {
    const features = room.features.split("|");

    let handleChange = (event) => {
        setRoom(room);
    }

    return (
        <div className="room col border rounded mx-2 g-0">
            <div className="w-100">
                <input 
                    type="radio" 
                    className="btn-check" 
                    name="room-options" 
                    id={"room-option-"+room.key}
                    autoComplete="off" 
                    onChange={handleChange} 
                />
                <label className="btn btn-outline-secondary w-100 rounded-0 rounded-top" htmlFor={"room-option-"+room.key}>
                    <FontAwesomeIcon icon={faHandPointer} className="fa-fw me-1" />
                    Select
                </label>
            </div>
            <div className="mt-2 px-2">
                <div className="text-center fw-bold border-bottom mb-2 pb-2">{room.name}</div>
                <div className="mb-2 border-bottom pb-2">
                    <div className="text-muted">Capacity</div>
                    <FontAwesomeIcon icon={faPeopleGroup} className="fa-fw me-1" /> {room.capacity}
                </div>
                <div>
                    <div className="text-muted">Features</div>
                    <ul className="g-0 ps-4">
                        {features.map((feature) => (
                            <li key={feature}>{feature}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    )
}

export default TripRoomsElement;