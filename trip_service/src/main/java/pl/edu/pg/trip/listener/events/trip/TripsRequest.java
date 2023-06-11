package pl.edu.pg.trip.listener.events.trip;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TripsRequest {
    private String destination;
    private String departure;
    private String startDate;
    private Integer adults;
    private Integer people3To9;
    private Integer people10To17;
}
