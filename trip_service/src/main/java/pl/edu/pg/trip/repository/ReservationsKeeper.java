package pl.edu.pg.trip.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.service.DelegatingHotelService;
import pl.edu.pg.trip.service.DelegatingTransportService;

@Component
public class ReservationsKeeper {

    private ReservationRepository reservationRepository;
    private final DelegatingTransportService delegatingTransportService;
    private final DelegatingHotelService delegatingHotelService;

    @Autowired
    public ReservationsKeeper(final ReservationRepository reservationRepository,
                              final DelegatingTransportService delegatingTransportService,
                              final DelegatingHotelService delegatingHotelService) {
        this.reservationRepository = reservationRepository;
        this.delegatingTransportService = delegatingTransportService;
        this.delegatingHotelService = delegatingHotelService;
    }

    @Scheduled(fixedDelay = 5000)
    public void deleteOutdated() {
        final var deletedReservations = reservationRepository.deleteOutdatedReservations();
        deletedReservations.forEach(reservation -> {
            delegatingTransportService.cancelReservation(reservation.getStartFlightReservation());
            delegatingTransportService.cancelReservation(reservation.getEndFlightReservation());
            delegatingHotelService.cancelReservation(reservation.getHotelReservation());
        });
    }
}
