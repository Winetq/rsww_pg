package pl.edu.pg.accommodation.hotel.notifier;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.hotel.listener.event.NotifyHotelAdded;

@Component
public class HotelEventNotifier {

    private final RabbitTemplate rabbitTemplate;
    private final String addHotelNotificationQueue;
    private final FanoutExchange fanoutExchange;
    @Autowired
    public HotelEventNotifier(final RabbitTemplate rabbitTemplate,
                              @Value("${spring.rabbitmq.queue.update.hotel.add}") final String addHotelQueue,
                              final FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.addHotelNotificationQueue = addHotelQueue;
        this.fanoutExchange = fanoutExchange;
    }

    public void notifyNewHotel(final HotelEntity hotel) {
        final var dto = NotifyHotelAdded.entityToDtoMapper().apply(hotel);
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), addHotelNotificationQueue, dto);
    }
}
