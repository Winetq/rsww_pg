import React from "react";
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTruckPlane, faCircleNotch } from "@fortawesome/free-solid-svg-icons";
import useFetch from "../hooks/useFetch";
import UrlBuilder from "./UrlBuilder";
import InfoToast from "./InfoToast";


const departurePlaces = [
    'Dojazd własny',
    'Warszawa (WAW)',
    'Gdańsk (GDA)',
    'Kraków (KRA)'
]

export default function TripDeparturePlaceInput({departurePlace, setDeparturePlace}) {
    let urlBuilder = new UrlBuilder();
    let {data, isPending, error} = useFetch(urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TRIPS_DEPARTURE_PLACES_URL'));

    return (
        <InputGroup>
            <InputGroup.Text>
                {
                isPending ?
                    <FontAwesomeIcon icon={faCircleNotch} className="fa-fw fa-spin" />
                :
                    <FontAwesomeIcon icon={faTruckPlane} className="fa-fw" />
                }
            </InputGroup.Text>
            <FloatingLabel
                controlId="departurePlaceInput"
                label="Departure Place"
            >
                <Form.Select 
                    type="select" 
                    onChange={(e) => setDeparturePlace(e.target.value)} 
                    value={departurePlace}
                    className={isPending ? 'disabled' : ''}
                    required
                >
                    <option value="all">Any</option>
                    {
                    isPending || error ?
                        null
                    :
                        data.map((place) => (
                            <option value={place} key={place}>{place}</option>
                        ))
                    }
                </Form.Select>
            </FloatingLabel>
            {
            !isPending && error ?
                <InfoToast variant="danger" content={"Loading departure places has failed :( \"" + error + "\""} />
            :
                null
            }
        </InputGroup>
    )
}