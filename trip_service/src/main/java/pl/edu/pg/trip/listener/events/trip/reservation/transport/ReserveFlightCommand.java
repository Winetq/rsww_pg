package pl.edu.pg.trip.listener.events.trip.reservation.transport;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ReserveFlightCommand {
    private long flightId;
    private long userId;
    private int numberOfPeople;
}
