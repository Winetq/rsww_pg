import React from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPeopleGroup, faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

import TripDestinationInput from "../components/TripDestinationInput";
import TripDateInput from "../components/TripDateInput";
import TripDeparturePlaceInput from "../components/TripDeparturePlaceInput";
import TripPeopleCollapse from "../components/TripPeopleCollapse";
import TripsListElement from "../components/TripsListElement";


export default class Home extends React.Component {
    
    state = {
        destination: "all",
        startDate: (new Date()).toISOString().substring(0, 10),
        departurePlace: "all",
        people3To9: 0,
        people10To18: 0,
        peopleOver18: 0,

        peopleCollapseOpen: false,
    }

    handleSubmit(event) {
        event.preventDefault();

        let urlParams = `?destination=${this.state.destination}&` + 
            `startDate=${this.state.startDate}&` +
            `people3To9=${this.state.people3To9}` + 
            `people10To18=${this.state.people10To18}` + 
            `peopleOver18=${this.state.peopleOver18}` + 
            `departurePlace=${this.state.departurePlace}`;
        window.location.replace('/trips' + urlParams);
    }

    render() {

        const trips = [
            {
                "id": 1,
                "img": "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                "hotel": "Beautifull Hotel",
                "location": "Egipt",
                "startDate": "2023-01-01",
                "endDate": "2023-01-08",
                "days": 7,
                "price": 1200
            },
            {
                "id": 2,
                "img": "https://theluxurytravelexpert.com/wp-content/uploads/2021/06/best-hotels-in-France.jpg",
                "hotel": "Beach Hotel",
                "location": "France",
                "startDate": "2023-02-01",
                "endDate": "2023-02-15",
                "days": 14,
                "price": 16000
            }
        ]

        return (
            <div className="home">
                <div className="mt-5 fw-bold fs-3 mb-3 border-bottom pb-1">
                    Configure your dream trip
                </div>
                <Form className="row row-cols-1 row-cols-lg-5 border-bottom mb-3 pb-3" onSubmit={this.handleSubmit.bind(this)}>

                    <div className="col mb-2 mb-lg-0">
                        <TripDestinationInput destination={this.state.destination} setDestination={(value) => this.setState({destination: value})}/>
                    </div>
                    
                    <div className="col mb-2 mb-lg-0">
                        <TripDateInput date={this.state.startDate} setDate={(value) => this.setState({startDate: value})} />
                    </div>

                    <div className="col mb-2 mb-lg-0">
                        <TripDeparturePlaceInput departurePlace={this.state.departurePlace} setDeparturePlace={(value) => this.setState({departurePlace: value})} />
                    </div>

                    <div className="col mb-2 mb-lg-0">
                    <Button
                        variant="outline-secondary"
                        onClick={() => this.setState({peopleCollapseOpen: !this.state.peopleCollapseOpen})}
                        aria-controls="participants-collapse"
                        aria-expanded={this.state.peopleCollapseOpen}
                        className="w-100 h-100"
                    >
                        <FontAwesomeIcon icon={faPeopleGroup} className="fa-fw me-1" />
                        Participants ({this.state.people3To9+this.state.people10To18+this.state.peopleOver18})
                    </Button>
                    </div>

                    <div className="col mb-2 mb-lg-0">
                    <Button type="submit" variant="primary" className="h-100 w-100">
                        <FontAwesomeIcon icon={faMagnifyingGlass} className="fw-fw me-1" />
                        Search
                    </Button>
                    </div>
                    
                    <div className="mt-2"></div>
                    <TripPeopleCollapse 
                        open={this.state.peopleCollapseOpen}
                        people3To9={this.state.people3To9}
                        setPeople3To9={(value) => this.setState({people3To9: value})}
                        people10To18={this.state.people10To18}
                        setPeople10To18={(value) => this.setState({people10To18: value})}
                        peopleOver18={this.state.peopleOver18}
                        setPeopleOver18={(value) => this.setState({peopleOver18: value})}
                    />                
                </Form>

                <div className="mt-2">
                    {trips.map(trip => (
                        <TripsListElement trip={trip} key={trip.id} />
                    ))}
                </div>
            </div>
        )
    }
}