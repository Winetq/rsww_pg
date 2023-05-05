package pl.edu.pg.transport.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.edu.pg.transport.entity.Transport;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CreateFlightEvent {
    private LocalDateTime departureDate;
    private String departureCountry;
    private String departureCity;
    private LocalDateTime arrivalDate;
    private String arrivalCountry;
    private String arrivalCity;

    public static Transport eventToEntity(CreateFlightEvent event) {
        return new Transport(event.departureDate, event.departureCountry, event.departureCity,
                event.arrivalDate, event.arrivalCountry, event.arrivalCity);
    }
}
