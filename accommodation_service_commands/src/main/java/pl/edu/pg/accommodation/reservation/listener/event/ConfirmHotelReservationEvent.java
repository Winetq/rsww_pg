package pl.edu.pg.accommodation.reservation.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;

import java.util.Objects;

public class ConfirmHotelReservationEvent extends GenericEvent {
    private Long reservationId;

    public ConfirmHotelReservationEvent() {
        super();
    }

    public ConfirmHotelReservationEvent(String source, Long reservationId) {
        super(source);
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConfirmHotelReservationEvent that = (ConfirmHotelReservationEvent) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reservationId);
    }

    @Override
    public String toString() {
        return "ConfirmHotelReservationEvent{" +
                "reservationId=" + reservationId +
                '}';
    }
}
