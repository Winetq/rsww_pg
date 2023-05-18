package pl.edu.pg.trip.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime arrivalDate;
    private Long travelTime;
    private Long placesCount;
    private Long placesOccupied;
}
