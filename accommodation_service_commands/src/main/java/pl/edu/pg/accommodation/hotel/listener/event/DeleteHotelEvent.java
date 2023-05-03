package pl.edu.pg.accommodation.hotel.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;

import java.util.Objects;

public class DeleteHotelEvent extends GenericEvent {

    private Long hotelId;

    public DeleteHotelEvent() {
        super();
    }

    public DeleteHotelEvent(String source, Long hotelId) {
        super(source);
        this.hotelId = hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeleteHotelEvent that = (DeleteHotelEvent) o;
        return Objects.equals(hotelId, that.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hotelId);
    }

    @Override
    public String toString() {
        return "DeleteHotelEvent{" +
                "hotelId=" + hotelId +
                '}';
    }
}
