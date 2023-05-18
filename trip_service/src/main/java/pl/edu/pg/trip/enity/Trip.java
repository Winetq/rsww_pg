package pl.edu.pg.trip.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private Long tripId;
    private Long hotelId;
    private Long startFlightId;
    private Long endFlightId;
}
