package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetDeparturesRequest;
import pl.edu.pg.transport.dto.GetDeparturesResponse;
import pl.edu.pg.transport.dto.GetFlightDetailsResponse;
import pl.edu.pg.transport.dto.GetFlightsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightsQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetFlightsQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightsQueryListener.class);

    private final FlightRepository repository;

    @Autowired
    public GetFlightsQueryListener(FlightRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightsQueue}")
    public List<GetFlightDetailsResponse> receiveMessage(GetFlightsQuery message) {
        List<Flight> flights = repository.findAll();
        logger.info("All flights were sent.");
        return GetFlightsResponse.entityToDtoMapper().apply(flights);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.flights.departures}")
    public GetDeparturesResponse getDestinations(GetDeparturesRequest request, Message message) {
        final var airports = repository.findAll().parallelStream()
                .map(flight -> flight.getDepartureAirport())
                .distinct()
                .collect(Collectors.toList());

        return GetDeparturesResponse.builder().departures(airports).build();
    }
}
