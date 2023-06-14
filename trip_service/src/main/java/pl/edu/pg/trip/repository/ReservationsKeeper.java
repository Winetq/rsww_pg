package pl.edu.pg.trip.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationsKeeper {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationsKeeper(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void deleteOutdated() {
        reservationRepository.deleteOutdatedReservations();
    }
}
