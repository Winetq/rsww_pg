import React from "react";
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTruckPlane } from "@fortawesome/free-solid-svg-icons";


export default function TripDeparturePlaceInput({departurePlace, setDeparturePlace}) {
    return (
        <InputGroup>
            <InputGroup.Text>
                <FontAwesomeIcon icon={faTruckPlane} className="fa-fw" />
            </InputGroup.Text>
            <FloatingLabel
                controlId="departurePlaceInput"
                label="Departure Place"
            >
                <Form.Select 
                    type="select" 
                    onChange={(e) => setDeparturePlace(e.target.value)} 
                    value={departurePlace}
                    required
                >
                    <option value="all">Any</option>
                    <option value="clientself">By Yourself</option>
                    <option value="city1">City 1</option>
                    <option value="city2">City 2</option>
                </Form.Select>
            </FloatingLabel>
        </InputGroup>
    )
}