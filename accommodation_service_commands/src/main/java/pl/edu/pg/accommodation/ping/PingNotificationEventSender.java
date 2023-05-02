package pl.edu.pg.accommodation.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PingNotificationEventSender {
    private final static Logger logger = LoggerFactory.getLogger(PingNotificationEventSender.class);

    private final RabbitTemplate template;
    private final Queue queue;

    @Autowired
    public PingNotificationEventSender(final RabbitTemplate template,
                                       @Value("#{pingNotificationQueue}") final Queue destinationQueue) {
        this.template = template;
        this.queue = destinationQueue;
    }
    public void send(String message) {
        this.template.convertAndSend(queue.getName(), message);
        logger.info("PingEvent: {}", message);
    }
}