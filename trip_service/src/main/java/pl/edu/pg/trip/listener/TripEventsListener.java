package pl.edu.pg.trip.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.trip.TripDetailsRequest;
import pl.edu.pg.trip.listener.events.trip.TripDetailsResponse;
import pl.edu.pg.trip.listener.events.trip.TripsRequest;
import pl.edu.pg.trip.listener.events.trip.TripsResponse;
import pl.edu.pg.trip.service.DelegatingHotelService;
import pl.edu.pg.trip.service.DelegatingTransportService;
import pl.edu.pg.trip.service.TripService;

import java.util.stream.Collectors;

@Component
public class TripEventsListener {
    private final static Logger log = LoggerFactory.getLogger(TripEventsListener.class);
    private final TripService tripService;
    private final DelegatingHotelService hotelService;
    private final DelegatingTransportService transportService;

    @Autowired
    public TripEventsListener(final TripService tripService,
                              final DelegatingHotelService hotelService,
                              final DelegatingTransportService transportService) {
        this.tripService = tripService;
        this.transportService = transportService;
        this.hotelService = hotelService;
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
        final var dtoTrips = trips.parallelStream().map(trip -> TripsResponse.toDtoMapper(
                transportService::getTransport,
                hotelService::getHotel)
                .apply(trip))
                .collect(Collectors.toList());
        final var response = TripsResponse.builder().trips(dtoTrips).build();
        log.debug("Response: {}", response);
        return response;
    }
}
