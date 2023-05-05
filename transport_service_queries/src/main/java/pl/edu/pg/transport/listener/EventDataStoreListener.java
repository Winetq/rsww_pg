package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.entity.Transport;
import pl.edu.pg.transport.repository.TransportRepository;

@Component
public class EventDataStoreListener {
    private final static Logger logger = LoggerFactory.getLogger(EventDataStoreListener.class);

    private final TransportRepository repository;

    @Autowired
    public EventDataStoreListener(TransportRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.eventDataStore}")
    public void receiveMessage(Message<Transport> message) {
        repository.save(message.getPayload());
    }
}
