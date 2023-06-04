package pl.edu.pg.gateway.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

@RestController
@RequestMapping("/api/trips")
class TripController {

    private final TripService tripService;

    @Autowired
    TripController(final TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    ResponseEntity<TripsResponse> getTrips() {
        final var maybeTrips = tripService.getTrips();
        return maybeTrips.map(tripsResponse -> new ResponseEntity<>(tripsResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
}
