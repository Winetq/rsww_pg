package pl.edu.pg.accommodation.reservation.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.event.Event;

@Data
@Builder
@Jacksonized
public class ConfirmHotelReservationEvent implements Event {
    private Long reservationId;
}
