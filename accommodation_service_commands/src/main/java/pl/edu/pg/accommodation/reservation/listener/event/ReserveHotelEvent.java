package pl.edu.pg.accommodation.reservation.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;

public class ReserveHotelEvent extends GenericEvent {
    private Long roomId;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private int numberOfPeople;

    public ReserveHotelEvent() {
        super();

    }
    public ReserveHotelEvent(String source, Long roomId, LocalDate reservationStart, LocalDate reservationEnd, int numberOfPeople) {
        super(source);
        this.roomId = roomId;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.numberOfPeople = numberOfPeople;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getReservationStart() {
        return reservationStart;
    }

    public void setReservationStart(LocalDate reservationStart) {
        this.reservationStart = reservationStart;
    }

    public LocalDate getReservationEnd() {
        return reservationEnd;
    }

    public void setReservationEnd(LocalDate reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReserveHotelEvent that = (ReserveHotelEvent) o;
        return numberOfPeople == that.numberOfPeople && Objects.equals(roomId, that.roomId) && Objects.equals(reservationStart, that.reservationStart) && Objects.equals(reservationEnd, that.reservationEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roomId, reservationStart, reservationEnd, numberOfPeople);
    }

    @Override
    public String toString() {
        return "ReserveHotelEvent{" +
                "roomId=" + roomId +
                ", reservationStart=" + reservationStart +
                ", reservationEnd=" + reservationEnd +
                ", numberOfPeople=" + numberOfPeople +
                '}';
    }

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
