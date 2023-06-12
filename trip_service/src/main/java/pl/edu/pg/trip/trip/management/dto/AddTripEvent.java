package pl.edu.pg.trip.trip.management.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.trip.enity.Trip;

import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class AddTripEvent {
    private Long hotelId;
    private Long startFlightId;
    private Long endFlightId;

    public static Function<AddTripEvent, Trip> eventToEntityMapper() {
        return event -> Trip.builder()
                .hotelId(event.hotelId)
                .startFlightId(event.startFlightId)
                .endFlightId(event.endFlightId)
                .build();
    }
}
