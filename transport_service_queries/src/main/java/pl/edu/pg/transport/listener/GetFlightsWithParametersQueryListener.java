package pl.edu.pg.transport.listener;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.transport.dto.GetFlightDetailsResponse;
import pl.edu.pg.transport.dto.GetFlightsResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.query.GetFlightsWithParametersQuery;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetFlightsWithParametersQueryListener {
    private final static Logger logger = LoggerFactory.getLogger(GetFlightsWithParametersQueryListener.class);

    private final FlightRepository repository;

    @Autowired
    public GetFlightsWithParametersQueryListener(FlightRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.getFlightsWithParametersQueue}")
    public List<GetFlightDetailsResponse> receiveMessage(GetFlightsWithParametersQuery message) {
        List<Flight> flights = repository.findAll();
        List<Flight> flightWithParameters = flights
                .stream()
                .filter(flight -> Strings.isEmpty(message.getDepartureAirport()) || flight.getDepartureAirport().equals(message.getDepartureAirport()) || message.getDepartureAirport().equals("all"))
                .filter(flight -> Strings.isEmpty(message.getArrivalAirport()) || flight.getArrivalAirport().equals(message.getArrivalAirport()) || message.getArrivalAirport().equals("all"))
                .filter(flight -> Strings.isEmpty(message.getDepartureDate()) || flight.getDepartureDate().startsWith(message.getDepartureDate()))
                .filter(flight -> Strings.isEmpty(message.getArrivalDate()) || flight.getArrivalDate().startsWith(message.getArrivalDate()))
                .collect(Collectors.toList());
        logger.info("Flights with these parameters were sent.");
        return GetFlightsResponse.entityToDtoMapper().apply(flightWithParameters);
    }
}
