package pl.edu.pg.trip.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.payments.PaymentRequest;
import pl.edu.pg.trip.listener.events.payments.PaymentResponse;
import pl.edu.pg.trip.listener.events.payments.PostPaymentResponse;
import pl.edu.pg.trip.listener.events.trip.TripDetailsRequest;
import pl.edu.pg.trip.listener.events.trip.TripDetailsResponse;
import pl.edu.pg.trip.listener.events.trip.TripsRequest;
import pl.edu.pg.trip.listener.events.trip.TripsResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.PostReservationRequest;
import pl.edu.pg.trip.listener.events.trip.reservation.PostReservationResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.UserReservationsRequest;
import pl.edu.pg.trip.service.DelegatingHotelService;
import pl.edu.pg.trip.service.DelegatingTransportService;
import pl.edu.pg.trip.service.PaymentService;
import pl.edu.pg.trip.service.TripService;

import java.util.stream.Collectors;

@Component
public class TripEventsListener {
    private final static Logger log = LoggerFactory.getLogger(TripEventsListener.class);
    private final TripService tripService;
    private final DelegatingHotelService hotelService;
    private final DelegatingTransportService transportService;
    private final PaymentService paymentService;

    @Autowired
    public TripEventsListener(final TripService tripService,
                              final DelegatingHotelService hotelService,
                              final DelegatingTransportService transportService,
                              final PaymentService paymentService) {
        this.tripService = tripService;
        this.transportService = transportService;
        this.hotelService = hotelService;
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.get.details}")
    public TripDetailsResponse getTripDetails(TripDetailsRequest request) {
        final var maybeTrip = tripService.getTrip(request.getTripId());
        if (maybeTrip.isEmpty()) {
            return TripDetailsResponse.builder().build();
        }
        final var trip = maybeTrip.get();
        return TripDetailsResponse.toDtoMapper(
                () -> hotelService.getHotel(trip.getHotelId()),
                transportService::getTransport
        ).apply(trip);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.get.all}")
    public TripsResponse getTrips(TripsRequest request, Message message) {
        log.debug("Request: {}", request);

        final var trips = tripService.getTrips(request).stream().collect(Collectors.toList());
        final var dtoTrips = trips.parallelStream().limit(200).map(trip -> TripsResponse.toDtoMapper(
                transportService::getTransport,
                hotelService::getHotel)
                .apply(trip))
                .collect(Collectors.toList());
        final var response = TripsResponse.builder().trips(dtoTrips).build();
        log.debug("Response: {}", response);
        return response;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trips.reserve}")
    public PostReservationResponse reserveTrip(PostReservationRequest request, Message message) {
        log.debug("Request: {}", request);
        final var reservationResult = tripService.reserveTrip(request.getTripId(), request.getFood(), request.getRoom(), request.getUser());
        return  PostReservationResponse.builder().reserved(reservationResult).build();
    }

    @RabbitListener(queues="${spring.rabbitmq.queue.trips.reservations}")
    public TripsResponse reservations(UserReservationsRequest request, Message message) {
        final var user = request.getUserId();
        final var trips = tripService.getReservations(user);
        return TripsResponse.builder()
                .trips(trips)
                .build();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trips.reservations.payment}")
    public PaymentResponse payForReservation(PaymentRequest request, Message message) {
        final var responseFromPaymentService = paymentService.paymentRequest();
        if (responseFromPaymentService.getStatus() >= 200 && responseFromPaymentService.getStatus() < 300) {
            tripService.confirmReservation(request.getReservationId(), request.getUserId());
            return PaymentResponse.builder().status(202).build();
        } else {
            return PaymentResponse.builder().status(500).build();
        }
    }
}
