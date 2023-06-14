package pl.edu.pg.trip.listener.events.trip.reservation.hotel;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CancelHotelReservation {
    private Long reservationId;
}
