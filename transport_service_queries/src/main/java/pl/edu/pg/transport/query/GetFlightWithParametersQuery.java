package pl.edu.pg.transport.query;

import lombok.Getter;

@Getter
public class GetFlightWithParametersQuery {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
}
