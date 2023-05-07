package pl.edu.pg.transport.command;

import lombok.Getter;
import pl.edu.pg.transport.entity.Flight;

@Getter
public class AddFlightCommand {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private int travelTime;
    private int placesCount;

    public static Flight commandToEntityMapper(AddFlightCommand command) {
        return new Flight(command.getDepartureAirport(), command.getArrivalAirport(),
                command.getDepartureDate(), command.getArrivalDate(),
                command.getTravelTime(), command.getPlacesCount());
    }
}
