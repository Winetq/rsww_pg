package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetFlightDetailsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightWithParametersQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GetFlightWithParametersQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightWithParametersQueryListener.class);

    private final FlightRepository repository;

    @Autowired
    public GetFlightWithParametersQueryListener(FlightRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightWithParametersQueue}")
    public GetFlightDetailsResponse receiveMessage(GetFlightWithParametersQuery message) {
        List<Flight> flights = repository.findAll();
        Optional<Flight> flightWithParameters = flights
                .stream()
                .filter(flight -> flight.getDepartureAirport().equals(message.getDepartureAirport()))
                .filter(flight -> flight.getArrivalAirport().equals(message.getArrivalAirport()))
                .filter(flight -> flight.getDepartureDate().equals(message.getDepartureDate()))
                .filter(flight -> flight.getArrivalDate().equals(message.getArrivalDate()))
                .findAny();
        if (flightWithParameters.isEmpty()) {
            logger.info("Flight with these parameters does not exist.");
            return GetFlightDetailsResponse.builder()
                    .id(-1)
                    .build();
        }
        logger.info("Flight with these parameters was found.");
        return GetFlightDetailsResponse.entityToDtoMapper().apply(flightWithParameters.get());
    }
}
