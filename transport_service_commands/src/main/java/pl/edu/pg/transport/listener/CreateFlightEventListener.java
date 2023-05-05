package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.entity.Transport;
import pl.edu.pg.transport.event.CreateFlightEvent;
import pl.edu.pg.transport.repository.TransportRepository;

@Component
public class CreateFlightEventListener {
    private final static Logger logger = LoggerFactory.getLogger(CreateFlightEventListener.class);

    private final TransportRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final Queue eventDataStore;

    @Autowired
    public CreateFlightEventListener(TransportRepository repository, RabbitTemplate rabbitTemplate, Queue eventDataStore) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.eventDataStore = eventDataStore;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.createFlightQueue}")
    public void receiveMessage(Message<CreateFlightEvent> message) {
        CreateFlightEvent event = message.getPayload();
        Transport transport = CreateFlightEvent.eventToEntity(event);
        Transport savedTransport = repository.save(transport);
        rabbitTemplate.convertAndSend(eventDataStore.getName(), savedTransport);
    }
}
