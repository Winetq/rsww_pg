package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.enity.Hotel;
import pl.edu.pg.trip.listener.events.hotel.GetHotelRequest;
import pl.edu.pg.trip.listener.events.hotel.GetHotelsRequest;
import pl.edu.pg.trip.listener.events.hotel.GetHotelsResponse;
import pl.edu.pg.trip.listener.events.hotel.HotelDetailsResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.PostReservationRequest;
import pl.edu.pg.trip.listener.events.trip.reservation.hotel.ReservationResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.hotel.ReserveHotelRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Component
public class DelegatingHotelService {
    private final Logger logger = LoggerFactory.getLogger(DelegatingHotelService.class);
    private final String allHotelsQueueName;
    private final String getHotelDetailsQueueName;
    private final String reserveHotelQueueName;
    private final String cancelHotelReservationQueueName;
    private final AsyncRabbitTemplate template;

    @Autowired
    public DelegatingHotelService(final AsyncRabbitTemplate asyncRabbitTemplate,
                                  @Value("${spring.rabbitmq.queue.hotel.get.all}") final String allHotelsQueueName,
                                  @Value("${spring.rabbitmq.queue.hotel.get.single}") final String getHotelDetailsQueueName,
                                  @Value("${spring.rabbitmq.queue.hotel.reservation}") final String reserveHotelQueueName,
                                  @Value("${spring.rabbitmq.queue.hotel.reservation.cancel}") final String cancelHotelReservationQueueName) {
        this.template = asyncRabbitTemplate;
        this.allHotelsQueueName = allHotelsQueueName;
        this.getHotelDetailsQueueName = getHotelDetailsQueueName;
        this.reserveHotelQueueName = reserveHotelQueueName;
        this.cancelHotelReservationQueueName = cancelHotelReservationQueueName;
    }

    public List<Hotel> getHotels(SearchParams params) {
        final var request = SearchParams.toRequest().apply(params);
        final CompletableFuture<GetHotelsResponse> completableRequest = template.convertSendAndReceiveAsType(
                allHotelsQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        try {
            final var response = completableRequest.get();
            final var hotels = GetHotelsResponse.dtoToEntityMapper().apply(response);
            logger.debug("Fetched {} hotels.", hotels.size());
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
            logger.debug("Fetched hotel {}", hotel);
            return Optional.of(hotel);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error when fetching hotel {} details.", id, e);
        }
        return Optional.empty();
    }

    public ReservationResponse reserve(Hotel hotel,
                                       PostReservationRequest.Room room,
                                       final String startDate,
                                       final String endDate,
                                       final String food,
                                       final Long user) {
        final var request = ReserveHotelRequest.builder()
                .hotelId(hotel.getId())
                .startDate(startDate)
                .endDate(endDate)
                .food(food)
                .user(user)
                .room(ReserveHotelRequest.Room.builder()
                        .capacity(room.getCapacity())
                        .features(room.getFeatures())
                        .name(room.getName())
                        .build())
                .build();
        final CompletableFuture<ReservationResponse> completableRequest = template.convertSendAndReceiveAsType(
                reserveHotelQueueName,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        try {
            final var response = completableRequest.get();
            logger.debug("Reservation response {}", response);
            return response;
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error when reserving hotel {}\nRoom: {}.", hotel, room, e);
        }
        return ReservationResponse.builder().success(false).build();
    }

    public boolean cancelReservation(Hotel hotel, PostReservationRequest.Room room) {
        return true;
    }

    @Data
    @Builder
    public static class SearchParams {
        private String destination;
        private String departure;
        private Integer adults;
        private Integer people3To9;
        private Integer people10To17;

        public static Function<SearchParams, GetHotelsRequest> toRequest() {
            return params -> GetHotelsRequest.builder()
                    .destination(params.destination)
                    .adults(params.adults)
                    .people3To9(params.people3To9)
                    .people10To17(params.people10To17)
                    .build();
        }
    }
}
