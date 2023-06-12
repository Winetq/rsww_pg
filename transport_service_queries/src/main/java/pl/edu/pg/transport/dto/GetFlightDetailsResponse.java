package pl.edu.pg.transport.dto;

import lombok.Builder;
import lombok.Getter;
import pl.edu.pg.transport.entity.Flight;

import java.util.function.Function;

@Builder
@Getter
public class GetFlightDetailsResponse {
    private final long id;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String departureDate;
    private final String arrivalDate;
    private final int travelTime;
    private final int placesCount;
    private final int placesOccupied;
    private final int price;

    public static Function<Flight, GetFlightDetailsResponse> entityToDtoMapper() {
        return flight -> GetFlightDetailsResponse
                .builder()
                .id(flight.getId())
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport())
                .departureDate(flight.getDepartureDate())
                .arrivalDate(flight.getArrivalDate())
                .travelTime(flight.getTravelTime())
                .placesCount(flight.getPlacesCount())
                .placesOccupied(flight.getPlacesOccupied())
                .price(flight.getPrice())
                .build();
    }
}
