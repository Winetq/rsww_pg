package pl.edu.pg.accommodation.reservation.listener.event.v2;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ReservationResponse {
    private Long reservationId;
    private Boolean success;
    private Double price;
}
