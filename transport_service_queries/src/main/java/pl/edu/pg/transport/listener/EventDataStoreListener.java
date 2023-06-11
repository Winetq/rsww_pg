package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.repository.FlightRepository;

@Component
public class EventDataStoreListener {
    private final static Logger logger = LoggerFactory.getLogger(EventDataStoreListener.class);

    private final FlightRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final Queue addFlightNotificationQueue;

    @Autowired
    public EventDataStoreListener(FlightRepository repository, RabbitTemplate rabbitTemplate, Queue addFlightNotificationQueue) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.addFlightNotificationQueue = addFlightNotificationQueue;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.addFlightDataStore}")
    public void saveFlight(Message<Flight> message) {
        Flight savedFlight = repository.save(message.getPayload());
        rabbitTemplate.convertAndSend(addFlightNotificationQueue.getName(), savedFlight);
        logger.info("A new flight was sent to AddFlightNotificationQueue.");
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.confirmFlightReservationDataStore}")
    public void updateFlight(Message<Flight> message) {
        repository.save(message.getPayload());
    }
}
