package pl.edu.pg.transport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetFlightDetailsResponse;
import pl.edu.pg.transport.dto.GetFlightsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightWithParametersQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetFlightWithParametersQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightWithParametersQueryListener.class);

    private final FlightRepository repository;

    @Autowired
    public GetFlightWithParametersQueryListener(FlightRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightWithParametersQueue}")
    public List<GetFlightDetailsResponse> receiveMessage(GetFlightWithParametersQuery message) {
        List<Flight> flights = repository.findAll();
        List<Flight> flightWithParameters = flights
                .stream()
                .filter(flight -> flight.getDepartureAirport().equals(message.getDepartureAirport()))
                .filter(flight -> flight.getArrivalAirport().equals(message.getArrivalAirport()))
                .filter(flight -> flight.getDepartureDate().startsWith(message.getDepartureDate()))
                .filter(flight -> flight.getArrivalDate().startsWith(message.getArrivalDate()))
                .collect(Collectors.toList());
        logger.info("Flights with these parameters were sent.");
        return GetFlightsResponse.entityToDtoMapper().apply(flightWithParameters);
    }
}
