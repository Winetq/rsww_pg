package pl.edu.pg.gateway.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

@RestController
@RequestMapping("/api/my-trips")
class MyTripController {

    private final MyTripService myTripService;

    @Autowired
    MyTripController(final MyTripService myTripService) {
        this.myTripService = myTripService;
    }

    @GetMapping
    ResponseEntity<TripsResponse> getMyTrips() {
        System.out.println("getMyTrips");
        final var maybeTrips = myTripService.getMyTrips();
        return maybeTrips.map(tripsResponse -> new ResponseEntity<>(tripsResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{id}")
    ResponseEntity<TripDetailsResponse> getMyTripDetails(@PathVariable Long id) {
        System.out.println("getMyTripDetails");
        final var maybeTrip = myTripService.getMyTripDetails(id);
        return maybeTrip.map(tripDetailsResponse -> new ResponseEntity<>(tripDetailsResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
