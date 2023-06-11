package pl.edu.pg.trip.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.TripDetailsRequest;
import pl.edu.pg.trip.listener.events.TripDetailsResponse;
import pl.edu.pg.trip.listener.events.TripsRequest;
import pl.edu.pg.trip.listener.events.TripsResponse;
import pl.edu.pg.trip.service.DelegatingHotelService;
import pl.edu.pg.trip.service.DelegatingTransportService;
import pl.edu.pg.trip.service.TripService;

import java.util.Random;
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
                () -> transportService.getTransport(trip.getStartFlightId())
        ).apply(trip);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.get.all}")
    public TripsResponse getTrips(TripsRequest request, Message message) {
        log.info("Request: {}", request);
        final Random random = new Random();
        final var trips = tripService.getTrips();

        final var response =  TripsResponse.builder()
                .trips(trips.stream().map(trip -> {
                    final var hotel = hotelService.getHotel(trip.getHotelId()).get();
                    final var hotelDto = TripsResponse.Hotel.builder()
                            .id(hotel.getId())
                            .name(hotel.getName())
                            .stars(hotel.getStars())
                            .place(hotel.getPlace())
                            .photo(hotel.getPhoto())
                            .build();
                    return TripsResponse.Trip.builder()
                                    .tripPrice(random.nextFloat(1000, 5000))
                                    .id(trip.getTripId())
                                    .dateStart("26.06.2022 16:59")
                                    .dateEnd("26.06.2022 16:59")
                                    .hotel(hotelDto)
                                    .build();

                        })
                        .collect(Collectors.toList()))
                .build();
        log.info("Response: {}", response);
        return response;
    }
}
