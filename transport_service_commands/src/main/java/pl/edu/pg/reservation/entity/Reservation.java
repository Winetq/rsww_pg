package pl.edu.pg.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.edu.pg.transport.entity.Flight;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flightId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "number_of_people")
    private int numberOfPeople;

    public Reservation(Flight flightId, Long userId, int numberOfPeople) {
        this.flightId = flightId;
        this.userId = userId;
        this.numberOfPeople = numberOfPeople;
    }
}
