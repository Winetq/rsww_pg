package pl.edu.pg.transport.query;

import lombok.Getter;

@Getter
public class GetFlightsWithParametersQuery {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
}
