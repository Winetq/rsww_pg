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
import pl.edu.pg.trip.service.TripService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TripEventsListener {
    private final static Logger log = LoggerFactory.getLogger(TripEventsListener.class);
    private final TripService tripService;
    private final DelegatingHotelService hotelService;

    @Autowired
    public TripEventsListener(final TripService tripService,
                              final DelegatingHotelService hotelService) {
        this.tripService = tripService;
        this.hotelService = hotelService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.get.details}")
    public TripDetailsResponse getTripDetails(TripDetailsRequest request) {
        return TripDetailsResponse.builder().build();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.get.all}")
    public TripsResponse getTrips(Message request) {
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
                                    .dateStart(LocalDate.of(2023, 6, 21))
                                    .dateEnd(LocalDate.of(2023, 6, 28))
                                    .hotel(hotelDto)
                                    .build();

                        })
                        .collect(Collectors.toList()))
                .build();
        return response;
    }
}
