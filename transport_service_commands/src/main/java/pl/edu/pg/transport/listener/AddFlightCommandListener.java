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
import pl.edu.pg.transport.command.AddFlightCommand;
import pl.edu.pg.transport.repository.FlightRepository;

@Component
public class AddFlightCommandListener {
    private final static Logger logger = LoggerFactory.getLogger(AddFlightCommandListener.class);

    private final FlightRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final Queue eventDataStore;

    @Autowired
    public AddFlightCommandListener(FlightRepository repository, RabbitTemplate rabbitTemplate, Queue eventDataStore) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.eventDataStore = eventDataStore;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.addFlightQueue}")
    public void receiveMessage(Message<AddFlightCommand> message) {
        AddFlightCommand command = message.getPayload();
        Flight flight = AddFlightCommand.commandToEntityMapper(command);
        Flight savedFlight = repository.save(flight);
        rabbitTemplate.convertAndSend(eventDataStore.getName(), savedFlight);
    }
}
