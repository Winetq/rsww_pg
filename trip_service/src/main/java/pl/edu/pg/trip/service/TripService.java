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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final Logger log = LoggerFactory.getLogger(TripService.class);
    private final TripRepository tripRepository;
    private final DelegatingTransportService transportService;
    private final DelegatingHotelService hotelService;
    private AtomicLong idAccessor;

    @Autowired
    public TripService(final TripRepository tripRepository,
                       final DelegatingHotelService delegatingHotelService,
                       final DelegatingTransportService transportService) {
        this.tripRepository = tripRepository;
        this.hotelService = delegatingHotelService;
        this.transportService = transportService;
    }

    public List<Trip> getTrips() {
        return tripRepository.findAllTrips();
    }

    public Optional<Trip> getTrip(Long tripId) {
        return tripRepository.findTrip(tripId);
    }

    public Trip addTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public void removeTrip(Long id) {
        tripRepository.delete(id);
    }
}
