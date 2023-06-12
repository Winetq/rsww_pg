package pl.edu.pg.transport.command;

import lombok.Getter;

@Getter
public class UpdateFlightPriceEvent {
    private long flightId;
    private int price;
}
