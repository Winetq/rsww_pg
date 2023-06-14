package pl.edu.pg.accommodation.event.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class RemoveReservation {
    private Long reservationId;
}
