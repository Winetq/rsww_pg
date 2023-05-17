import Collapse from 'react-bootstrap/Collapse';
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';


export default function TripPeopleCollapse({open,
                                            people3To9, setPeople3To9, 
                                            people10To17, setPeople10To17, 
                                            peopleOver18, setPeopleOver18}) 
{
    return (
        <Collapse in={open} className="w-100">
        <div id="participants-collapse" className="row row-cols-1 row-cols-lg-3">
            <div className="col text-center">
                <span>Children between 3 and 9 yo</span>
                <InputGroup>
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeople3To9(Math.max(0, people3To9-1))}
                    >
                        -
                    </Button>
                    <Form.Control 
                        type="number" 
                        min="0" 
                        max="100" 
                        value={people3To9} 
                        onChange={(e) => setPeople3To9(e.target.value)}
                        className="text-center"
                        readOnly
                        required
                    />
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeople3To9(Math.max(0, people3To9+1))}
                    >
                        +
                    </Button>
                </InputGroup>
            </div>
            <div className="col text-center">
                <span>Children between 10 and 17 yo</span>
                <InputGroup>
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeople10To17(Math.max(0, people10To17-1))}
                    >
                        -
                    </Button>
                    <Form.Control 
                        type="number" 
                        min="0" 
                        max="100" 
                        value={people10To17} 
                        onChange={(e) => setPeople10To17(e.target.value)} 
                        className="text-center"
                        readOnly
                        required
                    />
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeople10To17(Math.max(0, people10To17+1))}
                    >
                        +
                    </Button>
                </InputGroup>
            </div>
            <div className="col text-center">
                <span>People over 18 yo</span>
                <InputGroup>
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeopleOver18(Math.max(0, peopleOver18-1))}
                    >
                        -
                    </Button>
                    <Form.Control 
                        type="number" 
                        min="0" 
                        max="100" 
                        value={peopleOver18} 
                        onChange={(e) => setPeopleOver18(e.target.value)}
                        className="text-center"
                        readOnly
                        required
                    />
                    <Button 
                        variant="outline-secondary" 
                        onClick={() => setPeopleOver18(Math.max(0, peopleOver18+1))}
                    >
                        +
                    </Button>
                </InputGroup>
            </div>
        </div>
        </Collapse>
    )
}