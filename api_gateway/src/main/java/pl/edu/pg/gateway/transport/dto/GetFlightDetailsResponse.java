package pl.edu.pg.gateway.transport.dto;


import lombok.Getter;

@Getter
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
}
