package pl.edu.pg.reservation.command;

import lombok.Getter;
import pl.edu.pg.reservation.entity.Reservation;
import pl.edu.pg.transport.entity.Flight;

@Getter
public class ReserveFlightCommand {
    private long flightId;
    private long userId;
    private int numberOfPeople;

    public static Reservation commandToEntityMapper(ReserveFlightCommand command, Flight flight) {
        return new Reservation(flight, command.getUserId(), command.getNumberOfPeople());
    }
}
