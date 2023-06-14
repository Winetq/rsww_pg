package pl.edu.pg.accommodation.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.reservation.repository.ReservationRepository;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationNotifier notifier;
    @Autowired
    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationNotifier notifier) {
        this.reservationRepository = reservationRepository;
        this.notifier = notifier;
    }

    public Optional<ReservationEntity> makeReservation(final RoomEntity room,
                                             final String startDate,
                                             final String endDate,
                                             final int numberOfPeople) {
        final var reservationStart = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm"));
        final var reservationEnd = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm"));
        if (!isRoomAvailable(room, reservationStart, reservationEnd, numberOfPeople)) {
            Optional.empty();
        }

        final var reservation = new ReservationEntity();
        reservation.setReservationStart(reservationStart);
        reservation.setReservationStop(reservationEnd);
        reservation.setRoom(room);
        reservation.setNumberOfPeople(numberOfPeople);
        final var newReservation = reservationRepository.save(reservation);
        notifier.notifyNewHotel(reservation, room);
        return Optional.of(newReservation);
    }

    public Iterable<ReservationEntity> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<ReservationEntity> getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public void deleteReservation(final ReservationEntity reservation) {
        reservationRepository.delete(reservation);
        notifier.notifyReservationCanceled(reservation.getId());
    }

    private boolean isRoomAvailable(final RoomEntity room,
                                          final LocalDate startDate,
                                          final LocalDate endDate,
                                          final int numberOfPeople) {
        if (room.getCapacity() < numberOfPeople) {
            return false;
        }
        final Predicate<ReservationEntity> isReservationOverlapping = reservation -> {
              if (startDate.isBefore(reservation.getReservationStop()) && endDate.isAfter(startDate)) {
                  return true;
              }
              return false;
        };
        return reservationRepository.findAllByRoom(room)
                .stream()
                .filter(isReservationOverlapping)
                .findAny()
                .isEmpty();

    }
}
