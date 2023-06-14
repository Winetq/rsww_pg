package pl.edu.pg.accommodation.reservation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.accommodation.event.reservation.NewReservation;
import org.springframework.messaging.Message;
import pl.edu.pg.accommodation.event.reservation.RemoveReservation;

@Component
public class ReservationListener {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationListener(final ReservationRepository service) {
        this.reservationRepository = service;
    }

    @RabbitListener(queues = "${spring.rabbitmq.fanout.reservation.add}")
    public void addReservation(Message<NewReservation> reservationMessage) {
        final var reservation = NewReservation.eventToDtoMapper().apply(reservationMessage.getPayload());
        reservationRepository.addReservation(reservation);
    }

    @RabbitListener(queues = "${spring.rabbitmq.fanout.reservation.remove}")
    public void removeReservation(Message<RemoveReservation> reservationMessage) {
        reservationRepository.removeReservation(reservationMessage.getPayload().getReservationId());
    }
}
