package pl.edu.pg.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long hotelId;
    private Long roomId;
    private Long reservationId;
    private LocalDate start;
    private LocalDate end;
}
