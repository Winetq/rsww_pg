package pl.edu.pg.accommodation.room.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.Objects;
import java.util.function.Function;

public class AddRoomEvent extends GenericEvent {
    private int capacity;
    private float price;
    private String roomNumber;
    private Long hotelId;
    public AddRoomEvent() {
        super();
    }

    public AddRoomEvent(String source, int capacity, float price, String roomNumber, Long hotelId) {
        super(source);
        this.capacity = capacity;
        this.price = price;
        this.roomNumber = roomNumber;
        this.hotelId = hotelId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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
        AddRoomEvent that = (AddRoomEvent) o;
        return capacity == that.capacity && Float.compare(that.price, price) == 0 && Objects.equals(roomNumber, that.roomNumber) && Objects.equals(hotelId, that.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), capacity, price, roomNumber, hotelId);
    }

    @Override
    public String toString() {
        return "AddRoomEvent{" +
                "capacity=" + capacity +
                ", price=" + price +
                ", roomNumber='" + roomNumber + '\'' +
                ", hotelId=" + hotelId +
                '}';
    }

    public static Function<AddRoomEvent, RoomEntity> toEntityMapper(Function<Long, HotelEntity> hotelEntityFunction) {
        return event -> {
            final var room = new RoomEntity();
            room.setPrice(event.getPrice());
            room.setCapacity(event.getCapacity());
            room.setHotel(hotelEntityFunction.apply(event.getHotelId()));
            room.setRoomNumber(event.getRoomNumber());
            return room;
        };
    }
}
