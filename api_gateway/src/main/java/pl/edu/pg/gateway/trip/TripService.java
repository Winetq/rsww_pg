package pl.edu.pg.gateway.trip;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.trip.dto.TripDetailsRequest;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
import pl.edu.pg.gateway.trip.dto.TripsRequest;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

import java.util.Optional;

@Service
class TripService {
    private final RabbitTemplate rabbitTemplate;
    private final String getTripsQueueName;
    private final String getTripDetailsQueueName;

    @Autowired
    TripService(final RabbitTemplate rabbitTemplate,
                @Value("${spring.rabbitmq.queue.trip.get.all}") final String getTripsQueueName,
                @Value("${spring.rabbitmq.queue.trip.get.details}") final String getTripDetailsQueueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.getTripsQueueName = getTripsQueueName;
        this.getTripDetailsQueueName = getTripDetailsQueueName;
    }

    Optional<TripsResponse> getTrips() {
        final TripsRequest request = TripsRequest.builder().build();
        final TripsResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                getTripsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response);
    }

    Optional<TripDetailsResponse> getTripDetails(Long id) {
        final TripDetailsRequest request = TripDetailsRequest.builder().id(id).build();
        final TripDetailsResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                getTripDetailsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response);
    }

    ResponseEntity<String> getDestinations(String startDate, String endDate) {
        System.out.println(startDate);
        System.out.println(endDate);
        return new ResponseEntity<>("Not implemented yet!", HttpStatus.NOT_IMPLEMENTED);
    }

    ResponseEntity<String> getDestinations() {
        return new ResponseEntity<>("Not implemented yet!", HttpStatus.NOT_IMPLEMENTED);
    }

    ResponseEntity<String> reserveTrip(Long id) {
        System.out.println("Reserve a trip with id: " + id);
        return new ResponseEntity<>("Not implemented yet!", HttpStatus.NOT_IMPLEMENTED);
    }

    ResponseEntity<String> payForTrip(Long id) {
        System.out.println("Pay for a trip with id: " + id);
        return new ResponseEntity<>("Not implemented yet!", HttpStatus.NOT_IMPLEMENTED);
    }
}
