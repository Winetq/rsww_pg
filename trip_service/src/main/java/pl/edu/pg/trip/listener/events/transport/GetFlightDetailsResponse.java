package pl.edu.pg.trip.listener.events.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.trip.enity.Transport;

import java.util.function.Function;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class GetFlightDetailsResponse {
    private long id;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private int travelTime;
    private int placesCount;
    private int placesOccupied;
    private int price;

    public static Function<GetFlightDetailsResponse, Transport> toEntityMapper() {
        return response -> Transport.builder()
                .id(response.getId())
                .departureAirport(response.departureAirport)
                .arrivalAirport(response.arrivalAirport)
                .arrivalDate(response.arrivalDate)
                .departureDate(response.departureDate)
                .travelTime(response.travelTime)
                .placesCount(response.placesCount)
                .placesOccupied(response.placesOccupied)
                .price(response.price)
                .build();
    }
}
