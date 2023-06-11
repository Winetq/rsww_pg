package pl.edu.pg.gateway.trip;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.trip.dto.TripDetailsRequest;
import pl.edu.pg.gateway.trip.dto.TripDetailsResponse;
import pl.edu.pg.gateway.trip.dto.TripsRequest;
import pl.edu.pg.gateway.trip.dto.TripsResponse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TripService {
    private final Logger log = LoggerFactory.getLogger(TripService.class);
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

    public Optional<TripsResponse> getTrips(final SearchParams searchParams) {
        final TripsRequest request = SearchParams.requestMapper().apply(searchParams);
        final TripsResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                getTripsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response);
    }

    public Optional<TripDetailsResponse> getTrip(final Long id) {
        final var request = TripDetailsRequest.builder().tripId(id).build();
        final TripDetailsResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                getTripDetailsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response);
    }

    public List<String> getDestinations() {
        return ImmutableList.of("Egipt", "Turcja", "Włochy", "Hiszpania");
    }

    public List<String> getPossibleDepartures() {
        return ImmutableList.of("Dojazd własny", "Warszawa (WAW)", "Gdańsk (GDA)", "Kraków (KRA)");
    }

    @Data
    @Builder
    @Jacksonized
    public static class SearchParams {
        private String destination;
        private String departure;
        private String startDate;
        private Integer adults;
        private Integer people3To9;
        private Integer people10To17;

        public static Function<SearchParams, TripsRequest> requestMapper() {
            return params -> TripsRequest.builder()
                    .adults(params.adults)
                    .departure(params.departure)
                    .destination(params.destination)
                    .people10To17(params.people10To17)
                    .people3To9(params.people3To9)
                    .startDate(params.startDate)
                    .build();
        }
    }
}
