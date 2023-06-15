package pl.edu.pg.accommodation.hotel.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.hotel.listener.event.AddHotelEvent;
import pl.edu.pg.accommodation.hotel.listener.event.DeleteHotelEvent;
import pl.edu.pg.accommodation.hotel.listener.event.UpdateRoomPriceEvent;
import pl.edu.pg.accommodation.hotel.service.HotelService;
import pl.edu.pg.accommodation.room.service.RoomService;

import java.util.stream.IntStream;

@Component
public class HotelEventsListener {

    private final static Logger log = LoggerFactory.getLogger(HotelEventsListener.class);
    private final HotelService hotelService;
    private final RoomService roomService;

    @Autowired
    public HotelEventsListener(final HotelService hotelService,
                               final RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.add}")
    public void addHotel(Message<AddHotelEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var hotel = AddHotelEvent.toEntityMapper().apply(eventMessage.getPayload());
        hotelService.addHotel(hotel);
        for (var roomTemplate : eventMessage.getPayload().getRooms()) {
            IntStream.range(0, roomTemplate.getNumberOfRooms())
                    .forEach(i -> roomService.addRoom(AddHotelEvent.Room.toEntityMapper(() -> hotel)
                            .apply(roomTemplate)));
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.delete}")
    public void removeHotel(Message<DeleteHotelEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var maybeHotel = hotelService.findHotelById(eventMessage.getPayload().getHotelId());
        maybeHotel.ifPresent(hotelService::deleteHotel);
    }

    @RabbitListener(queues="${spring.rabbitmq.queue.update.hotel.price}")
    public void updateHotelPrice(Message<UpdateRoomPriceEvent> eventMessage) {
        final var hotel = hotelService.findHotelById(eventMessage.getPayload().getHotelId());
        if (hotel.isEmpty()) {
            return;
        }
        final var maybeRoom = roomService.findAllRoomsInHotel(hotel.get()).stream()
                .filter(room -> room.getCapacity() == eventMessage.getPayload().getRoom().getCapacity())
                .filter(room -> room.getName().equals(eventMessage.getPayload().getRoom().getName()))
                .filter(room -> room.getFeatures().equals(eventMessage.getPayload().getRoom().getFeatures()))
                .findAny();
        if (maybeRoom.isEmpty()) {
            return;
        }
        roomService.update(maybeRoom.get(), eventMessage.getPayload().getNewPrice());
    }
}
