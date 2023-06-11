package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.trip.enity.Trip;
import pl.edu.pg.trip.repository.TripRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final Logger log = LoggerFactory.getLogger(TripService.class);
    private final TripRepository tripRepository;
    private final DelegatingTransportService transportService;
    private final DelegatingHotelService hotelService;
    private static boolean FIRST_REQUEST = true;
    @Autowired
    public TripService(final TripRepository tripRepository,
                       final DelegatingHotelService delegatingHotelService,
                       final DelegatingTransportService transportService) {
        this.tripRepository = tripRepository;
        this.hotelService = delegatingHotelService;
        this.transportService = transportService;
    }

    public List<Trip> getTrips() {
        if (FIRST_REQUEST) {
            FIRST_REQUEST = true;
            return populateWithTrips();
        }
        return tripRepository.findAllTrips();
    }

    public Optional<Trip> getTrip(Long tripId) {
        return tripRepository.findTrip(tripId);
    }

    private List<Trip> populateWithTrips() {
        final var hotels = hotelService.getHotels();
        final var trips = hotels.stream()
                .map(accommodation -> Trip.builder()
                        .hotelId(accommodation.getId())
                        .build())
                .toList();

        trips.forEach(tripRepository::save);
        return trips;
    }
}
