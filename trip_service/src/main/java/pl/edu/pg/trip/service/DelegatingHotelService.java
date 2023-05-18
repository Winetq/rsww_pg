package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.enity.Hotel;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.listener.events.GetHotelRequest;
import pl.edu.pg.trip.listener.events.GetHotelsRequest;
import pl.edu.pg.trip.listener.events.GetHotelsResponse;
import pl.edu.pg.trip.listener.events.HotelDetailsResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class DelegatingHotelService {
    private final Logger logger = LoggerFactory.getLogger(DelegatingHotelService.class);
    private final String allHotelsQueueName;
    private final String getHotelDetailsQueueName;
    private final AsyncRabbitTemplate template;

    @Autowired
    public DelegatingHotelService(final AsyncRabbitTemplate asyncRabbitTemplate,
                                  @Value("${spring.rabbitmq.queue.hotel.get.all}") final String allHotelsQueueName,
                                  @Value("${spring.rabbitmq.queue.hotel.get.single}") final String getHotelDetailsQueueName) {
        this.template = asyncRabbitTemplate;
        this.allHotelsQueueName = allHotelsQueueName;
        this.getHotelDetailsQueueName = getHotelDetailsQueueName;
    }

    public List<Hotel> getHotels() {
        final var request = new GetHotelsRequest();
        final CompletableFuture<GetHotelsResponse> completableRequest = template.convertSendAndReceiveAsType(
                allHotelsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        try {
            final var response = completableRequest.get();
            final var hotels = GetHotelsResponse.dtoToEntityMapper().apply(response);
            logger.info("Fetched {} hotels.", hotels.size());
            return hotels;
        } catch (ExecutionException | InterruptedException e) {
             logger.error("Cannot get all hotels from hotel service.", e);
        }
        return ImmutableList.of();
    }

    public Optional<Hotel> getHotel(Long id) {
        final var request = GetHotelRequest.builder().id(id).build();
        final CompletableFuture<HotelDetailsResponse> completableRequest = template.convertSendAndReceiveAsType(
                getHotelDetailsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        try {
            final var response = completableRequest.get();
            final var hotel = HotelDetailsResponse.dtoToEntityMapper().apply(response);
            logger.info("Fetched hotel {}", hotel);
            return Optional.of(hotel);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error when fetching hotel {} details.", id, e);
        }
        return Optional.empty();
    }
}
