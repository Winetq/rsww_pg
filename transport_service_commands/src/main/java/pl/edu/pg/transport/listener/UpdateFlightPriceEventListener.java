package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.command.UpdateFlightPriceEvent;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.Optional;

@Component
public class UpdateFlightPriceEventListener {
    private final static Logger logger = LoggerFactory.getLogger(UpdateFlightPriceEventListener.class);

    private final FlightRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final Queue updateFlightQueue;

    @Autowired
    public UpdateFlightPriceEventListener(FlightRepository repository, RabbitTemplate rabbitTemplate, Queue updateFlightQueue) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.updateFlightQueue = updateFlightQueue;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.updateFlightPriceQueue}")
    public void receiveMessage(Message<UpdateFlightPriceEvent> message) {
        UpdateFlightPriceEvent event = message.getPayload();
        Optional<Flight> maybeFlight = repository.findById(event.getFlightId());
        if (maybeFlight.isEmpty()) {
            logger.info("Cannot update a price for flight that does not exist. FlightId: {}", event.getFlightId());
        } else {
            Flight flight = maybeFlight.get();
            flight.updatePrice(event.getPrice());
            Flight savedFlight = repository.save(flight);
            rabbitTemplate.convertAndSend(updateFlightQueue.getName(), savedFlight);
            logger.info("Updated a price for flightId: {}", event.getFlightId());
        }
    }
}
