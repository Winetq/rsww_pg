package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetFlightDetailsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightDetailsQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.Optional;

@Component
public class GetFlightDetailsQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightDetailsQueryListener.class);

    private final FlightRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GetFlightDetailsQueryListener(FlightRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightDetailsQueue}")
    public void receiveMessage(Message<GetFlightDetailsQuery> message) {
        long flightId = message.getPayload().getId();
        String sourceQueue = message.getPayload().getSource();
        Optional<Flight> maybeFlight = repository.findById(flightId);
        if (maybeFlight.isPresent()) {
            rabbitTemplate.convertAndSend(sourceQueue, GetFlightDetailsResponse.entityToDtoMapper().apply(maybeFlight.get()));
            logger.info("Flight with id {} was sent to the {} queue.", flightId, sourceQueue);
        } else {
            rabbitTemplate.convertAndSend(sourceQueue, "Not Found");
            logger.info("Flight with id {} does not exist.", flightId);
        }
    }
}
