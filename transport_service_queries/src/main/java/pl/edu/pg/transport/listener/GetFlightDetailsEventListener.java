package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.entity.Transport;
import pl.edu.pg.transport.event.GetFlightDetailsEvent;
import pl.edu.pg.transport.repository.TransportRepository;

import java.util.Optional;

@Component
public class GetFlightDetailsEventListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightDetailsEventListener.class);

    private final TransportRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GetFlightDetailsEventListener(TransportRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightDetailsQueue}")
    public void receiveMessage(Message<GetFlightDetailsEvent> message) {
        long transportId = message.getPayload().getId();
        String sourceQueue = message.getPayload().getSource();
        Optional<Transport> maybeTransport = repository.findById(transportId);
        if (maybeTransport.isPresent()) {
            rabbitTemplate.convertAndSend(sourceQueue, maybeTransport.get());
            logger.info("Transport with id {} was sent to the {} queue.", transportId, sourceQueue);
        } else {
            rabbitTemplate.convertAndSend(sourceQueue, "Not Found");
            logger.info("Transport with id {} does not exist.", transportId);
        }
    }
}
