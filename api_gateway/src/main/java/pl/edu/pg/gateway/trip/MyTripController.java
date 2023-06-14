package pl.edu.pg.gateway.trip;

import jakarta.websocket.server.PathParam;
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

import java.util.List;

@RestController
@RequestMapping("/api/my-trips")
class MyTripController {

    private final MyTripService myTripService;

    @Autowired
    MyTripController(final MyTripService myTripService) {
        this.myTripService = myTripService;
    }

    @GetMapping
    ResponseEntity<List<TripsResponse.Trip>> getMyTrips(@RequestParam(value = "user", required = true) Long userId) {
        final var maybeTrips = myTripService.getMyTrips(userId);
        return new ResponseEntity<>(maybeTrips.get().getTrips(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    ResponseEntity<TripDetailsResponse> getMyTripDetails(@PathVariable Long id) {
        System.out.println("getMyTripDetails");
        final var maybeTrip = myTripService.getMyTripDetails(id);
        return maybeTrip.map(tripDetailsResponse -> new ResponseEntity<>(tripDetailsResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
