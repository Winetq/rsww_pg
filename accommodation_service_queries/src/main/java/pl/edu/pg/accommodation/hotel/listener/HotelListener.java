package pl.edu.pg.accommodation.hotel.listener;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.event.GetDestinationsRequest;
import pl.edu.pg.accommodation.event.GetDestinationsResponse;
import pl.edu.pg.accommodation.event.GetHotelDetailResponseEvent;
import pl.edu.pg.accommodation.event.GetHotelDetailsEvent;
import pl.edu.pg.accommodation.event.GetHotelsEvent;
import pl.edu.pg.accommodation.event.GetHotelsResponseEvent;
import pl.edu.pg.accommodation.hotel.service.HotelService;
import pl.edu.pg.accommodation.model.Hotel;
import pl.edu.pg.accommodation.model.Room;

import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class HotelListener {
    private final static Logger log = LoggerFactory.getLogger(HotelListener.class);
    private final HotelService hotelService;
    @Autowired
    public HotelListener(final HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.get.all}")
    public GetHotelsResponseEvent getHotelsListener(GetHotelsEvent request, Message message) {
        log.debug("Received request for all hotels. Message correlation id: {}",
                message.getMessageProperties().getCorrelationId());
        final var hotelsPredicate = hotelPredicate(request);

        final var hotels = hotelService.getAllHotels().stream()
                .filter(hotelsPredicate)
                .collect(Collectors.toList());
        return GetHotelsResponseEvent.entityToDtoMapper().apply(hotels);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.get.single}")
    public GetHotelDetailResponseEvent getHotelDetailsListener(GetHotelDetailsEvent request, Message message) {
        log.debug("Received request hotel details. Message correlation id: {}",
                message.getMessageProperties().getCorrelationId());
        final var hotelId = request.getId();
        final var maybeHotel = hotelService.getHotel(hotelId);
        if (maybeHotel.isEmpty()) {
            log.error("No hotel with id {} has been found. Returning empty response.", hotelId);
            return GetHotelDetailResponseEvent.builder()
                    .id(-1)
                    .build();
        }
        return GetHotelDetailResponseEvent.entityToDtoMapper().apply(maybeHotel.get());
    }

    private Predicate<Hotel> hotelPredicate(GetHotelsEvent request) {
        final Predicate<Hotel> destinationPredicate = hotel -> {
            if (Strings.isEmpty(request.getDestination())) {
                return true;
            }
            if (request.getDestination().equals("all")) {
                return true;
            }
            return request.getDestination().equals(hotel.getCountry());
        };
        final Predicate<Hotel> capacityCondition = hotel -> {
            final var requiredCapacity = request.getAdults() + request.getPeople3To9() + request.getPeople10To17();
            final var hotelCapacity = hotel.getRooms().stream().map(Room::getCapacity).reduce(0, (a, b) -> a+b);
            return hotelCapacity >= requiredCapacity;
        };
        return destinationPredicate
                .and(capacityCondition);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.destinations}")
    public GetDestinationsResponse getDestinations(GetDestinationsRequest request, Message message) {
        return GetDestinationsResponse.builder().destinations(hotelService.getDestinations().stream().toList()).build();
    }
}
