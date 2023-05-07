package pl.edu.pg.transport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flights")
@NoArgsConstructor
@Getter
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "departure_airport")
    private String departureAirport;

    @Column(name = "arrival_airport")
    private String arrivalAirport;

    @Column(name = "departure_date")
    private String departureDate;

    @Column(name = "arrival_date")
    private String arrivalDate;

    @Column(name = "travel_time")
    private int travelTime;

    @Column(name = "places_count")
    private int placesCount;

    @Column(name = "places_occupied")
    private int placesOccupied;

    public Flight(String departureAirport, String arrivalAirport,
                  String departureDate, String arrivalDate,
                  int travelTime, int placesCount) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.travelTime = travelTime;
        this.placesCount = placesCount;
        this.placesOccupied = 0;
    }
}
