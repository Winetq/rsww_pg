package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public GetFlightDetailsQueryListener(FlightRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightDetailsQueue}")
    public GetFlightDetailsResponse receiveMessage(GetFlightDetailsQuery message) {
        long flightId = message.getId();
        Optional<Flight> maybeFlight = repository.findById(flightId);
        if (maybeFlight.isEmpty()) {
            logger.info("Flight with id {} does not exist.", flightId);
            return GetFlightDetailsResponse.builder()
                    .id(-1)
                    .build();
        }
        logger.info("Flight with id {} was found.", flightId);
        return GetFlightDetailsResponse.entityToDtoMapper().apply(maybeFlight.get());
    }
}
