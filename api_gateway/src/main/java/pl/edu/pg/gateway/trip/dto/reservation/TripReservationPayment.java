package pl.edu.pg.gateway.trip.dto.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TripReservationPayment {
    private final Integer reservationId;
    private final Long userId;
}
