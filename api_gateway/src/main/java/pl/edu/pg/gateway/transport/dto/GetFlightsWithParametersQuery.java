package pl.edu.pg.gateway.transport.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetFlightsWithParametersQuery {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
}
