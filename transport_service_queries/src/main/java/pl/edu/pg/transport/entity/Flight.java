package pl.edu.pg.transport.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("flights")
public class Flight {

    @Id
    private Long id;

    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private int travelTime;
    private int placesCount;
    private int placesOccupied;
    private int price;
}
