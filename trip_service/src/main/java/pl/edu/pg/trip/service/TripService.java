package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.trip.enity.Hotel;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.enity.Trip;
import pl.edu.pg.trip.listener.events.trip.TripsRequest;
import pl.edu.pg.trip.repository.TripRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
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

    public List<Trip> getTrips(TripsRequest request) {
        final var requiredPlaces = request.getPeople10To17() + request.getPeople3To9() + request.getAdults();
        final var trips = tripRepository.findAllTrips();
        final var hotels = hotelService.getHotels(DelegatingHotelService.SearchParams.builder()
                .adults(request.getAdults())
                .destination(request.getDestination())
                .people3To9(request.getPeople3To9())
                .people10To17(request.getPeople10To17())
                .build())
                .stream()
                .map(hotel -> hotel.getId())
                .collect(Collectors.toSet());
        final var transports = transportService.getTransports(request.getDeparture(),
                "",
                request.getStartDate(),
                "")
                .stream()
                .filter(transport -> transport.getPlacesCount() - transport.getPlacesOccupied() >= requiredPlaces)
                .map(Transport::getId)
                .collect(Collectors.toSet());

        return trips.stream()
                .filter(trip -> hotels.contains(trip.getHotelId()))
                .filter(trip -> transports.contains(trip.getStartFlightId()))
                .collect(Collectors.toList());
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
