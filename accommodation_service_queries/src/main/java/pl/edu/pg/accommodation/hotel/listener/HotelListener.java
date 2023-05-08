package pl.edu.pg.accommodation.hotel.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.event.GetHotelDetailResponseEvent;
import pl.edu.pg.accommodation.event.GetHotelDetailsEvent;
import pl.edu.pg.accommodation.event.GetHotelsEvent;
import pl.edu.pg.accommodation.event.GetHotelsResponseEvent;
import pl.edu.pg.accommodation.hotel.service.HotelService;

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
        final var hotels = hotelService.getAllHotels();
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
}
