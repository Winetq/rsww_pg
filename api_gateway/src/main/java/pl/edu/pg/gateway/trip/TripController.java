package pl.edu.pg.gateway.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
import pl.edu.pg.gateway.trip.dto.TripsResponse;
import pl.edu.pg.gateway.trip.dto.reservation.PostReservationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(final TripService tripService) {
        this.tripService = tripService;
    }
    @GetMapping
    ResponseEntity<List<TripsResponse.Trip>> getTrips(@RequestParam(value = "destination", required = false) String destination,
                                                      @RequestParam(value = "departurePlace", required = false) String departure,
                                                      @RequestParam(value = "startDate", required = false) String startDate,
                                                      @RequestParam(value = "adults", required = false) Integer adults,
                                                      @RequestParam(value = "people3To9", required = false) Integer people3To9,
                                                      @RequestParam(value = "people10To17", required = false) Integer people10To17) {
        final var searchParams = TripService.SearchParams.builder()
                .adults(adults)
                .departure(departure)
                .destination(destination)
                .people10To17(people10To17)
                .people3To9(people3To9)
                .startDate(startDate)
                .build();
        final var maybeTrips = tripService.getTrips(searchParams);
        final List<TripsResponse.Trip> trips;
        if (maybeTrips.isPresent()) {
            trips = maybeTrips.get().getTrips();
        } else {
            trips = ImmutableList.of();
        }
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{id}")
    ResponseEntity<TripDetailsResponse> getTrip(@PathVariable("id") Long tripId) {
        final var maybeTrip = tripService.getTrip(tripId);
        return ResponseEntity.ok(maybeTrip.get());
    }

    @GetMapping
    @RequestMapping("/destinations")
    ResponseEntity<List<String>> getDestinations() {
        return ResponseEntity.ok(tripService.getDestinations());
    }

    @GetMapping
    @RequestMapping("/departure-places")
    ResponseEntity<List<String>> getPossibleDepartures() {
        return ResponseEntity.ok(tripService.getPossibleDepartures());
    }

    @PostMapping(consumes = MediaType.ALL_VALUE)
    @RequestMapping("/{id}/reserve/")
    ResponseEntity reserveTrip(@PathVariable("id") Long tripId,
                               @RequestBody String request) {
        final var objectMapper = new ObjectMapper();
        try {
            PostReservationRequest postRequest = objectMapper.readValue(request, PostReservationRequest.class);
            tripService.reserve(tripId, postRequest);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("{id}/payment")
    ResponseEntity<String> payForTrip(@PathVariable Long id) {
        return ResponseEntity.ok("");
    }
}
