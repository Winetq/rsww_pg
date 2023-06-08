package pl.edu.pg.gateway.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
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

    @GetMapping("{id}")
    ResponseEntity<TripDetailsResponse> getTripDetails(@PathVariable Long id) {
        final var maybeTrip = tripService.getTripDetails(id);
        return maybeTrip.map(tripDetailsResponse -> new ResponseEntity<>(tripDetailsResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("destinations")
    ResponseEntity<String> getDestinations(@RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate) {
        if (startDate == null || endDate == null) {
            return tripService.getDestinations();
        }
        return tripService.getDestinations(startDate, endDate);
    }

    @PostMapping("{id}/reserve")
    ResponseEntity<String> reserveTrip(@PathVariable Long id) {
        return tripService.reserveTrip(id);
    }

    @PostMapping("{id}/payment")
    ResponseEntity<String> payForTrip(@PathVariable Long id) {
        return tripService.payForTrip(id);
    }
    
}
