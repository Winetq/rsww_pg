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
    private final String priceUpdatedQueue;
    @Autowired
    public RoomEventNotifier(final RabbitTemplate rabbitTemplate,
                             @Value("${spring.rabbitmq.queue.update.hotel.room.add}") final String addRoomQueue,
                             @Value("${spring.rabbitmq.queue.update.hotel.price.notify}") final String priceUpdatedQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.addRoomQueue = addRoomQueue;
        this.priceUpdatedQueue = addRoomQueue;
    }

    public void notifyAddRoom(final RoomEntity room) {
        final var dto = NotifyRoomAdded.entityToDtoMapper(() -> room.getHotel().getId())
                .apply(room);
        rabbitTemplate.convertAndSend(addRoomQueue, dto);
    }

    public void notifyPriceUpdate(final RoomEntity room) {
        if (room == null) return;
        final var dto = NotifyRoomAdded.entityToDtoMapper(() -> room.getHotel().getId())
                .apply(room);
        rabbitTemplate.convertAndSend(addRoomQueue, dto);
    }
}
