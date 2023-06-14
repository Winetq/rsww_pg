package pl.edu.pg.trip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.enity.Trip;
import pl.edu.pg.trip.listener.events.trip.TripsRequest;
import pl.edu.pg.trip.listener.events.trip.TripsResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.HotelReservationResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.PostReservationRequest;
import pl.edu.pg.trip.repository.ReservationRepository;
import pl.edu.pg.trip.repository.TripRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    private final PaymentService paymentService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public TripService(final TripRepository tripRepository,
                       final DelegatingHotelService delegatingHotelService,
                       final DelegatingTransportService transportService,
                       final PaymentService paymentService,
                       final ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.hotelService = delegatingHotelService;
        this.transportService = transportService;
        this.paymentService = paymentService;
        this.reservationRepository = reservationRepository;
    }

    public List<Trip> getTrips(TripsRequest request) {
        int requiredPlaces = request.getPeople10To17() == null ? 0 : request.getPeople10To17();
        requiredPlaces += request.getPeople3To9() == null ? 0 : request.getPeople3To9();
        requiredPlaces += request.getAdults() == null ? 0 : request.getAdults();
        final var minPlaces = requiredPlaces;
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
                .filter(transport -> transport.getPlacesCount() - transport.getPlacesOccupied() >= minPlaces)
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

    public boolean reserveTrip(final Long tripId, final String foodOption, final PostReservationRequest.Room room,
                               final Long user) {
        final var trip = getTrip(tripId);
        final var hotel = hotelService.getHotel(trip.get().getHotelId()).get();
        final var startFlight = transportService.getTransport(trip.get().getStartFlightId()).get();
        final var endFlight = transportService.getTransport(trip.get().getStartFlightId()).get();

        final var hotelReserved = hotelService.reserve(hotel, room, startFlight.getDepartureDate(),
                endFlight.getDepartureDate(), foodOption, user);
        if (!hotelReserved.getSuccess()) {
            return false;
        }

        final Optional<Long> startFlightReserved = transportService.reserve(startFlight.getId(), 1, user);
        if (startFlightReserved.isEmpty()) {
            hotelService.cancelReservation(hotelReserved.getReservationId());
            return false;
        }

        final Optional<Long> endFlightReserved = transportService.reserve(endFlight.getId(), 1, user);
        if (endFlightReserved.isEmpty()) {
            hotelService.cancelReservation(hotelReserved.getReservationId());
            transportService.cancelReservation(startFlightReserved.get());
            return false;
        }

        final var reservation = ReservationRepository.Reservation.builder()
                .startFlightReservation(startFlightReserved.get())
                .endFlightReservation(endFlightReserved.get())
                .userId(user)
                .hotelId(hotel.getId())
                .hotelReservation(hotelReserved.getReservationId())
                .reserved(LocalDateTime.now().plusMinutes(1))
                .startFlightId(startFlight.getId())
                .endFlightId(endFlight.getId())
                .payed(false)
                .tripId(Math.toIntExact(tripId))
                .price(hotelReserved.getPrice() + startFlight.getPrice() + endFlight.getPrice())
                .build();
        reservationRepository.save(reservation);
        return true;
    }

    public List<TripsResponse.Trip> getReservations(Long userId) {
        final var reservations = reservationRepository.getUserReservations(userId);
        final var trips = tripRepository.findTrips(reservations.stream().map(ReservationRepository.Reservation::getTripId).collect(Collectors.toSet()));
        return reservations.stream().map(reservation -> {
            final var maybeTrip = trips.parallelStream().filter(t -> t.getTripId().equals(reservation.getTripId())).findFirst();
            if (maybeTrip.isEmpty()) {
                return null;
            }
            final var trip = maybeTrip.get();
            final var hotel = hotelService.getHotel(trip.getHotelId()).get();
            final var startFlight = transportService.getTransport(trip.getStartFlightId()).get();
            final var endFlight = transportService.getTransport(trip.getEndFlightId()).get();
            return TripsResponse.Trip.builder()
                    .id(reservation.getReservationId())
                    .tripPrice(reservation.getPrice())
                    .dateStart(startFlight.getDepartureDate())
                    .dateEnd(endFlight.getDepartureDate())
                    .hotel(TripsResponse.Hotel.builder()
                            .id(hotel.getId())
                            .name(hotel.getName())
                            .stars(hotel.getStars())
                            .place(hotel.getPlace())
                            .photo(hotel.getPhoto())
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    public void confirmReservation(Integer reservation, Long user) {
        reservationRepository.markAsPayed(reservation);
    }
}
