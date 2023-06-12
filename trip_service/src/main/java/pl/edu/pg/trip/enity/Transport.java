package pl.edu.pg.trip.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    private String departureAirport;
    private String arrivalAirport;
    private String arrivalDate;
    private String departureDate;
    private int travelTime;
    private int placesCount;
    private int placesOccupied;
    private int price;
}
