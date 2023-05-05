package pl.edu.pg.accommodation.reservation.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.event.Event;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.time.LocalDate;
import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class ReserveHotelEvent implements Event {
    private Long roomId;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private int numberOfPeople;

    public static Function<ReserveHotelEvent, ReservationEntity> toEntityMapper(Function<Long, RoomEntity> roomEntityFunction) {
        return (event) -> {
            final var reservation = new ReservationEntity();
            reservation.setRoom(roomEntityFunction.apply(event.getRoomId()));
            reservation.setReservationStart(event.getReservationStart());
            reservation.setReservationStop(event.getReservationEnd());
            reservation.setNumberOfPeople(event.numberOfPeople);
            return reservation;
        };
    }
}
