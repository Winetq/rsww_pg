package pl.edu.pg.accommodation.reservation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.reservation.listener.event.CancelHotelReservationEvent;
import pl.edu.pg.accommodation.reservation.listener.event.ConfirmHotelReservationEvent;
import pl.edu.pg.accommodation.reservation.listener.event.ReserveHotelEvent;
import pl.edu.pg.accommodation.reservation.service.ReservationService;
import pl.edu.pg.accommodation.room.service.RoomService;

import java.util.NoSuchElementException;

// TODO RSWW-13: implement reservations handling
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
    public void reserveHotelListener(Message<ReserveHotelEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
        final var reservation = ReserveHotelEvent.toEntityMapper(
                roomId -> roomService.findRoom(roomId).orElseThrow(() -> {
                    log.error("Cannot create the reservation for room that not exists. RoomId: {}", roomId);
                    return new NoSuchElementException("No room with given id has been found.");
                })
        ).apply(eventMessage.getPayload());
        reservationService.makeReservation(reservation);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.reservation.cancel}")
    public void cancelHotelReservationListener(Message<CancelHotelReservationEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.hotel.reservation.confirm}")
    public void confirmHotelReservationListener(Message<ConfirmHotelReservationEvent> eventMessage) {
        log.debug("Event: {}", eventMessage);
    }
}
