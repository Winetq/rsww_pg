import React from "react";
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendar } from "@fortawesome/free-solid-svg-icons";


export default function TripDateInput({date, setDate}) {
    return (
        <InputGroup>
            <InputGroup.Text>
                <FontAwesomeIcon icon={faCalendar} className="fa-fw" />
            </InputGroup.Text>
            <FloatingLabel
                controlId="startDateInput"
                label="Start Date"
            >
                <Form.Control 
                    type="date" 
                    onChange={(e) => setDate(e.target.value)} 
                    value={date}
                    required
                />
            </FloatingLabel>
        </InputGroup>
    )
}