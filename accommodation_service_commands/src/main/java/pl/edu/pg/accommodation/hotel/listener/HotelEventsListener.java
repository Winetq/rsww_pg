package pl.edu.pg.accommodation.hotel.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.hotel.listener.event.AddHotelEvent;
import pl.edu.pg.accommodation.hotel.listener.event.DeleteHotelEvent;
import pl.edu.pg.accommodation.hotel.service.HotelService;

@Component
public class HotelEventsListener {

    private final static Logger log = LoggerFactory.getLogger(HotelEventsListener.class);
    private final HotelService hotelService;

    @Autowired
    public HotelEventsListener(final HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.add}")
    public void addHotel(Message<AddHotelEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var hotel = AddHotelEvent.toEntityMapper().apply(eventMessage.getPayload());
        hotelService.addHotel(hotel);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.delete}")
    public void removeHotel(Message<DeleteHotelEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var maybeHotel = hotelService.findHotelById(eventMessage.getPayload().getHotelId());
        maybeHotel.ifPresent(hotelService::deleteHotel);
    }
}
