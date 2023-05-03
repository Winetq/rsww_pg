package pl.edu.pg.accommodation.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.reservation.repository.ReservationRepository;

import java.util.Optional;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationEntity makeReservation(final ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    public Iterable<ReservationEntity> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<ReservationEntity> getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public void deleteReservation(final ReservationEntity reservation) {
        reservationRepository.delete(reservation);
    }
}
