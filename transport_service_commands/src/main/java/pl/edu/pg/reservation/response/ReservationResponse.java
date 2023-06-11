package pl.edu.pg.reservation.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReservationResponse {
    private boolean status;
    private String message;
}
