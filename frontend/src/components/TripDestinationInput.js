import FloatingLabel from 'react-bootstrap/FloatingLabel';
import InputGroup from 'react-bootstrap/InputGroup';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLocationDot } from "@fortawesome/free-solid-svg-icons";


export default function TripDeparturePlaceInput({destination, setDestination}) {
    return (
        <InputGroup>
            <InputGroup.Text>
                <FontAwesomeIcon icon={faLocationDot} className="fa-fw" />
            </InputGroup.Text>
            <FloatingLabel
                controlId="desetinationInput"
                label="Destination"
            >
                <Form.Select 
                    type="select" 
                    onChange={(e) => setDestination(e.target.value)} 
                    value={destination}
                    required
                >
                    <option value="all">Any</option>
                    <option value="country">Country 1</option>
                </Form.Select>
            </FloatingLabel>
        </InputGroup>
    )
}