package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.entity.Transport;
import pl.edu.pg.transport.event.GetFlightsEvent;
import pl.edu.pg.transport.repository.TransportRepository;

import java.util.List;

@Component
public class GetFlightsEventListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightsEventListener.class);

    private final TransportRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GetFlightsEventListener(TransportRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightsQueue}")
    public void receiveMessage(Message<GetFlightsEvent> message) {
        List<Transport> transports = repository.findAll();
        rabbitTemplate.convertAndSend(message.getPayload().getSource(), transports);
        logger.info("All transports were sent to the {} queue.", message.getPayload().getSource());
    }
}
