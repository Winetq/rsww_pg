package pl.edu.pg.trip.listener.events.transport;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class GetFlightsQueryRequest {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
}
