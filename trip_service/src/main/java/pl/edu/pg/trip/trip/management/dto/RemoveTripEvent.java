package pl.edu.pg.trip.trip.management.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class RemoveTripEvent {
    private Long tripId;
}
