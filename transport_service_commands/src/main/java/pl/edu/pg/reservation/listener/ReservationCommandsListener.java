package pl.edu.pg.reservation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.reservation.command.CancelFlightReservationCommand;
import pl.edu.pg.reservation.command.ConfirmFlightReservationCommand;
import pl.edu.pg.reservation.command.ReserveFlightCommand;
import pl.edu.pg.reservation.entity.Reservation;
import pl.edu.pg.reservation.repository.ReservationRepository;
import pl.edu.pg.reservation.response.ReservationResponse;
import pl.edu.pg.transport.entity.Flight;
import pl.edu.pg.transport.repository.FlightRepository;

import java.util.Optional;

@Component
public class ReservationCommandsListener {
    private final static Logger logger = LoggerFactory.getLogger(ReservationCommandsListener.class);

    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Queue confirmFlightReservationDataStore;

    @Autowired
    public ReservationCommandsListener(FlightRepository flightRepository, ReservationRepository reservationRepository,
                                       RabbitTemplate rabbitTemplate, Queue confirmFlightReservationDataStore) {
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.confirmFlightReservationDataStore = confirmFlightReservationDataStore;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.reserveFlightQueue}")
    public ReservationResponse reserveFlightListener(ReserveFlightCommand message) {
        Optional<Flight> maybeFlight = flightRepository.findById(message.getFlightId());
        if (maybeFlight.isEmpty()) {
            return ReservationResponse
                    .builder()
                    .status(false)
                    .message("Cannot create a reservation for flight that does not exist. FlightId: " + message.getFlightId())
                    .build();
        }

        Flight flight = maybeFlight.get();
        if (flight.getPlacesOccupied() + message.getNumberOfPeople() > flight.getPlacesCount()) {
            return ReservationResponse
                    .builder()
                    .status(false)
                    .message("Cannot create a reservation because there are not enough places. FlightId: " + message.getFlightId())
                    .build();
        }

        Reservation reservation = ReserveFlightCommand.commandToEntityMapper(message, flight);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse
                .builder()
                .status(true)
                .message("Created reservation: " + savedReservation.getId())
                .build();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.cancelFlightReservationQueue}")
    public ReservationResponse cancelFlightReservationListener(CancelFlightReservationCommand message) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(message.getReservationId());
        if (maybeReservation.isEmpty()) {
            return ReservationResponse
                    .builder()
                    .status(false)
                    .message("Cannot cancel a reservation that does not exist. ReservationId: " + message.getReservationId())
                    .build();
        }

        reservationRepository.delete(maybeReservation.get());
        return ReservationResponse
                .builder()
                .status(true)
                .message("Canceled reservation: " + message.getReservationId())
                .build();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.confirmFlightReservationQueue}")
    public ReservationResponse confirmFlightReservationListener(ConfirmFlightReservationCommand message) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(message.getReservationId());
        if (maybeReservation.isEmpty()) {
            return ReservationResponse
                    .builder()
                    .status(false)
                    .message("Cannot confirm a reservation that does not exist. ReservationId: " + message.getReservationId())
                    .build();
        }

        Reservation reservation = maybeReservation.get();
        Flight flight = reservation.getFlightId();
        flight.reservePlaces(reservation.getNumberOfPeople());
        Flight savedFlight = flightRepository.save(flight);
        rabbitTemplate.convertAndSend(confirmFlightReservationDataStore.getName(), savedFlight);
        return ReservationResponse
                .builder()
                .status(true)
                .message("Confirmed reservation: " + message.getReservationId())
                .build();
    }
}
