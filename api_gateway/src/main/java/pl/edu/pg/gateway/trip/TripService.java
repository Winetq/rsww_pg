package pl.edu.pg.gateway.trip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.trip.dto.TripsRequest;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

import java.util.Optional;

@Service
class TripService {
    private final Logger log = LoggerFactory.getLogger(TripService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String getTripsQueueName;

    @Autowired
    TripService(final RabbitTemplate rabbitTemplate,
                @Value("${spring.rabbitmq.queue.trip.get.all}") final String getTripsQueueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.getTripsQueueName = getTripsQueueName;
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
}
