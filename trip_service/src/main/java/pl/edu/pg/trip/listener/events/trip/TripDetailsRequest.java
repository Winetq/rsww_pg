package pl.edu.pg.trip.listener.events.trip;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TripDetailsRequest {
    private long tripId;
}
