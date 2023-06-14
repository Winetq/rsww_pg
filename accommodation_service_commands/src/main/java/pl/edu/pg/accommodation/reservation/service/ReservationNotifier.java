package pl.edu.pg.accommodation.reservation.service;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.time.format.DateTimeFormatter;

@Component
public class ReservationNotifier {

    private final RabbitTemplate rabbitTemplate;
    private final String reserveQueue;
    private final String removeQueue;
    private final FanoutExchange fanoutExchange;
    @Autowired
    public ReservationNotifier(final RabbitTemplate rabbitTemplate,
                              @Value("${spring.rabbitmq.fanout.reservation.add}") final String reserveQueue,
                              @Value("${spring.rabbitmq.fanout.reservation.remove}") final String removeQueue,
                              final FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.reserveQueue = reserveQueue;
        this.removeQueue = removeQueue;
        this.fanoutExchange = fanoutExchange;
    }

    public void notifyNewHotel(ReservationEntity reservation, RoomEntity room) {
        final var dto = ReservationNotification.builder()
                .hotelId(room.getHotel().getId())
                .roomId(room.getId())
                .reservationId(reservation.getId())
                .start(reservation.getReservationStart().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .end(reservation.getReservationStop().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();
        rabbitTemplate.convertAndSend(reserveQueue, dto);
    }

    public void notifyReservationCanceled(Long reservationId) {
        final var dto = CancelReservationNotification.builder().reservationId(reservationId).build();
        rabbitTemplate.convertAndSend(removeQueue, dto);
    }

    @Data
    @Builder
    @Jacksonized
    public static class CancelReservationNotification {
        private Long reservationId;
    }

    @Data
    @Builder
    @Jacksonized
    public static class ReservationNotification {
        private Long hotelId;
        private Long roomId;
        private Long reservationId;
        private String start;
        private String end;
    }
}