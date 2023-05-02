package pl.edu.pg.accommodation.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PingEventListener {
    private final static Logger logger = LoggerFactory.getLogger(PingEventListener.class);

    private final PingRepository repository;
    private final PingNotificationEventSender eventSender;

    @Autowired
    public PingEventListener(final PingRepository repository,
                             final PingNotificationEventSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }

    @RabbitListener(queues = "${spring.rabbitmq.ping.queue}")
    public void receiveMessage(String message) {
        logger.info("I'm here!!! Your message: {}", message);
        final var entity = new PingEntity();
        entity.setPingDateTime(LocalDateTime.now());
        entity.setMessage(message);
        repository.save(entity);
        logger.info("Pings so far: {}", repository.findAll());
        eventSender.send(entity.toString());
    }
}
