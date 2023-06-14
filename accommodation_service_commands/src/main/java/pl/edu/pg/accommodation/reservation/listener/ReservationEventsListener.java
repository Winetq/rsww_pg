package pl.edu.pg.accommodation.reservation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.reservation.listener.event.CancelHotelReservationEvent;
import pl.edu.pg.accommodation.reservation.listener.event.ConfirmHotelReservationEvent;
import pl.edu.pg.accommodation.reservation.listener.event.v2.PostReservationRequest;
import pl.edu.pg.accommodation.reservation.listener.event.v2.ReservationResponse;
import pl.edu.pg.accommodation.reservation.service.ReservationService;
import pl.edu.pg.accommodation.room.service.RoomService;

@Component
public class ReservationEventsListener {
    private final static Logger log = LoggerFactory.getLogger(ReservationEventsListener.class);
    private final ReservationService reservationService;
    private final RoomService roomService;

    @Autowired
    public ReservationEventsListener(final ReservationService reservationService, final RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.reservation}")
    public ReservationResponse reserveHotelListener(Message<PostReservationRequest> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var room = roomService.findMatchingRoom(eventMessage.getPayload().getHotelId(),
                eventMessage.getPayload().getRoom().getCapacity(),
                eventMessage.getPayload().getRoom().getName(),
                eventMessage.getPayload().getRoom().getFeatures());
        if (room.isEmpty()) {
            log.error("Cannot find the room for given parameters: {}", eventMessage.getPayload());
            return ReservationResponse.builder().success(false).build();
        }
        final var reservation = reservationService.makeReservation(room.get(),
                eventMessage.getPayload().getStartDate(),
                eventMessage.getPayload().getEndDate(),
                1);
        if (reservation.isEmpty()) {
            log.error("Cannot reserve the room for given parameters: {}", eventMessage.getPayload());
            return ReservationResponse.builder().success(false).build();
        }
        return ReservationResponse.builder().success(true).reservationId(reservation.get().getId()).build();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.reservation.cancel}")
    public void cancelHotelReservationListener(Message<CancelHotelReservationEvent> eventMessage) {
        final var reservationId = eventMessage.getPayload().getReservationId();
        final var reservation = reservationService.getReservation(reservationId);
        if (reservation.isPresent()) {
            reservationService.deleteReservation(reservation.get());
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.reservation.confirm}")
    public void confirmHotelReservationListener(Message<ConfirmHotelReservationEvent> eventMessage) {
        log.warn("Event: {} is obsolete. Should not be propagated", eventMessage);
    }
}
