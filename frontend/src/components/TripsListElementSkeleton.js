import 'react-loading-skeleton/dist/skeleton.css'
import Skeleton from "react-loading-skeleton"
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";


export default function TripsListElementSkeleton() {

    return (
        <Card className="w-75 mx-auto p-3 shadow mb-3">
            <div className="row g-0">
                <div className="col-md-5">
                    <Skeleton className="h-100" style={{"minHeight": "200px"}}/>
                </div>
                <div className="col-md-7">
                    <Card.Body>
                        <Card.Title className="border-bottom">
                            <h5>
                                <Skeleton />
                            </h5>
                        </Card.Title>
                        <Card.Text>
                            <ListGroup className="list-group-flush">
                                <ListGroup.Item className="px-1">
                                    <Skeleton />
                                </ListGroup.Item>
                                <ListGroup.Item className="px-1">
                                    <Skeleton />
                                </ListGroup.Item>
                                <ListGroup.Item className="px-1">
                                    <Skeleton />
                                </ListGroup.Item>
                            </ListGroup>
                        </Card.Text>
                    </Card.Body>
                </div>
            </div>
        </Card>
    )
}