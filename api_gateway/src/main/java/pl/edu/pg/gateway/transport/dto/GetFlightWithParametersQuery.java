package pl.edu.pg.gateway.transport.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetFlightWithParametersQuery {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
}
