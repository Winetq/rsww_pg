package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.listener.events.transport.GetFlightDetailsQuery;
import pl.edu.pg.trip.listener.events.transport.GetFlightDetailsResponse;
import pl.edu.pg.trip.listener.events.transport.GetFlightsQueryRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class DelegatingTransportService {
    private final Logger logger = LoggerFactory.getLogger(DelegatingTransportService.class);
    private final String flightDetailsQueue;
    private final String flightsWithQueryQueue;
    private final AsyncRabbitTemplate template;

    @Autowired
    public DelegatingTransportService(final AsyncRabbitTemplate asyncRabbitTemplate,
                                      @Value("${spring.rabbitmq.queue.getFlightDetailsQueue}") final String flightDetailsQueue,
                                      @Value("${spring.rabbitmq.queue.getFlightsWithParametersQueue}") final String flightsWithQueryQueue) {
        this.template = asyncRabbitTemplate;
        this.flightDetailsQueue = flightDetailsQueue;
        this.flightsWithQueryQueue = flightsWithQueryQueue;
    }

    public List<Transport> getTransports() {
        return ImmutableList.of();
    }

    public Optional<Transport> getTransport(Long id) {
        Transport transport = null;
        final var request = GetFlightDetailsQuery.builder().id(id).build();
        final CompletableFuture<GetFlightDetailsResponse> completableRequest = template.convertSendAndReceiveAsType(
                flightDetailsQueue,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        try {
            final var response = completableRequest.get();
            transport = GetFlightDetailsResponse.toEntityMapper().apply(response);
        }  catch (ExecutionException | InterruptedException e) {
            logger.error("Error when fetching hotel {} details.", id, e);
        }
        return Optional.ofNullable(transport);
    }

    public List<Transport> getTransports(final String departureAirport,
                                         final String arrivalAirport,
                                         final String departureDate,
                                         final String arrivalDate) {
        GetFlightsQueryRequest query = GetFlightsQueryRequest.builder()
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureDate(departureDate)
                .arrivalDate(arrivalDate)
                .build();

        CompletableFuture<List<GetFlightDetailsResponse>> completableRequest = template.convertSendAndReceiveAsType(
                flightsWithQueryQueue,
                query,
                new ParameterizedTypeReference<>() {
                });
        try {
            return completableRequest.get().stream().map(dto -> Transport.builder()
                            .departureAirport(dto.getDepartureAirport())
                            .arrivalAirport(dto.getArrivalAirport())
                            .arrivalDate(dto.getArrivalDate())
                            .departureDate(dto.getDepartureDate())
                            .travelTime(dto.getTravelTime())
                            .placesCount(dto.getPlacesCount())
                            .placesOccupied(dto.getPlacesOccupied())
                            .price(dto.getPrice())
                            .id(dto.getId())
                            .build())
                    .collect(Collectors.toList());
        }  catch (ExecutionException | InterruptedException e) {
            logger.error("Error when fetching transports with query parameters departureAirport={};" +
                    " arrivalAirport={}; departureDate={}; arrivalDate={}",
                    departureAirport,
                    arrivalAirport,
                    departureAirport,
                    arrivalAirport,
                    e
            );
        }
        return ImmutableList.of();
    }


}
