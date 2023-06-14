package pl.edu.pg.gateway.trip;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.trip.dto.TripDetailsRequest;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
import pl.edu.pg.gateway.trip.dto.TripsRequest;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

import java.util.Optional;

@Service
class MyTripService {
    private final RabbitTemplate rabbitTemplate;
    private final String getUserReservations; // TODO: change a queue
    private final String getTripDetailsQueueName; // TODO: change a queue

    @Autowired
    MyTripService(final RabbitTemplate rabbitTemplate,
                @Value("${spring.rabbitmq.queue.trips.reservations}") final String getUserReservations,
                @Value("${spring.rabbitmq.queue.trip.get.details}") final String getTripDetailsQueueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.getUserReservations = getUserReservations;
        this.getTripDetailsQueueName = getTripDetailsQueueName;
    }

    Optional<TripsResponse> getMyTrips(Long userId) {
        final GetUserReservations dto = GetUserReservations.builder().userId(userId).build();
        final TripsResponse tripsResponse = rabbitTemplate.convertSendAndReceiveAsType(
                getUserReservations,
                dto,
                new ParameterizedTypeReference<>() {
                }
        );

        return Optional.ofNullable(tripsResponse);
    }

    Optional<TripDetailsResponse> getMyTripDetails(Long id) {
        final TripDetailsRequest request = TripDetailsRequest.builder().tripId(id).build();
        final TripDetailsResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                getTripDetailsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response);
    }

    @Data
    @Builder
    @Jacksonized
    public static class GetUserReservations {
        private Long userId;
    }
}
