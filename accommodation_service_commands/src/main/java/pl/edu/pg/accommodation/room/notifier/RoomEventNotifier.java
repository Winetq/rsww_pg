package pl.edu.pg.accommodation.room.notifier;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.room.listener.event.NotifyRoomAdded;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

@Component
public class RoomEventNotifier {
    private final RabbitTemplate rabbitTemplate;
    private final String addRoomQueue;
    @Autowired
    public RoomEventNotifier(final RabbitTemplate rabbitTemplate,
                             @Value("${spring.rabbitmq.queue.update.hotel.room.add}") final String addRoomQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.addRoomQueue = addRoomQueue;
    }

    public void notifyAddRoom(final RoomEntity room) {
        final var dto = NotifyRoomAdded.entityToDtoMapper(() -> room.getHotel().getId())
                .apply(room);
        rabbitTemplate.convertAndSend(addRoomQueue, dto);
    }
}
