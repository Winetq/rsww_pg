package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetFlightsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightsQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.List;

@Component
public class GetFlightsQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightsQueryListener.class);

    private final FlightRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GetFlightsQueryListener(FlightRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightsQueue}")
    public void receiveMessage(Message<GetFlightsQuery> message) {
        List<Flight> flights = repository.findAll();
        rabbitTemplate.convertAndSend(message.getPayload().getSource(), GetFlightsResponse.entityToDtoMapper().apply(flights));
        logger.info("All flights were sent to the {} queue.", message.getPayload().getSource());
    }
}
