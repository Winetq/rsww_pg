package pl.edu.pg.accommodation.room.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.hotel.service.HotelService;
import pl.edu.pg.accommodation.room.listener.event.AddRoomEvent;
import pl.edu.pg.accommodation.room.listener.event.DeleteRoomEvent;
import pl.edu.pg.accommodation.room.service.RoomService;

import java.util.NoSuchElementException;

@Component
public class RoomEventsListener {
    private final static Logger log = LoggerFactory.getLogger(RoomEventsListener.class);
    private final RoomService roomService;
    private final HotelService hotelService;

    @Autowired
    public RoomEventsListener(final RoomService roomService, final HotelService hotelService) {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.room.add}")
    public void addRoomListener(Message<AddRoomEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var room = AddRoomEvent.toEntityMapper(
                hotelId -> hotelService.findHotelById(hotelId).orElseThrow(() -> {
                    log.error("Cannot add room to the hotel that not exists. Missing hotel id: {}", hotelId);
                    return new NoSuchElementException("No hotel with id: " + hotelId);
                })
        ).apply(eventMessage.getPayload());
        roomService.addRoom(room);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.room.delete}")
    public void deleteRoomListener(Message<DeleteRoomEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var maybeRoom = roomService.findRoom(eventMessage.getPayload().getRoomId());
        maybeRoom.ifPresent(roomService::deleteRoom);
    }
}
