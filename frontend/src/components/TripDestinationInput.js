import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLocationDot, faCircleNotch } from "@fortawesome/free-solid-svg-icons";
import useFetch from "../hooks/useFetch";
import UrlBuilder from "./UrlBuilder";
import InfoToast from './InfoToast';


const destinations = [
    'Egipt',
    'Turcja',
    'Włochy',
    'Hiszpania'
]

export default function TripDeparturePlaceInput({destination, setDestination}) {
    let urlBuilder = new UrlBuilder();
    let {data, isPending, error} = useFetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_DESTINATIONS_URL'));

    return (
        <InputGroup>
            <InputGroup.Text>
                {
                isPending ?
                    <FontAwesomeIcon icon={faCircleNotch} className="fa-fw fa-spin" />
                :
                    <FontAwesomeIcon icon={faLocationDot} className="fa-fw" />
                }
            </InputGroup.Text>
            <FloatingLabel
                controlId="desetinationInput"
                label="Destination"
            >
                <Form.Select 
                    type="select" 
                    onChange={(e) => setDestination(e.target.value)} 
                    value={destination}
                    className={isPending ? "disabled" : ""}
                    required
                >
                    <option value="all">Any</option>
                    {
                    isPending || error ?
                        null
                    :
                        data.map((dest) => (
                            <option value={dest} key={dest}>{dest}</option>
                        ))
                    }
                </Form.Select>
            </FloatingLabel>
            {
            !isPending && error ?
                <InfoToast variant="danger" content={"Loading destinations has failed :( \"" + error + "\""} />
            :
                null
            }
        </InputGroup>
    )
}