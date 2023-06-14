package pl.edu.pg.accommodation.event.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class NewReservation {
    private Long hotelId;
    private Long roomId;
    private Long reservationId;
    private String start;
    private String end;

    public static Function<NewReservation, Reservation> eventToDtoMapper() {
        return event -> Reservation.builder()
                .reservationId(event.getReservationId())
                .hotelId(event.getHotelId())
                .roomId(event.getRoomId())
                .start(LocalDate.parse(event.getStart(), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .end(LocalDate.parse(event.getEnd(), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();
    }
}
