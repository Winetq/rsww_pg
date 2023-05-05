package pl.edu.pg.accommodation.room.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.event.Event;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class AddRoomEvent implements Event {
    private int capacity;
    private float price;
    private Long hotelId;
    private String features;

    public static Function<AddRoomEvent, RoomEntity> toEntityMapper(Function<Long, HotelEntity> hotelEntityFunction) {
        return event -> {
            final var room = new RoomEntity();
            room.setPrice(event.getPrice());
            room.setCapacity(event.getCapacity());
            room.setHotel(hotelEntityFunction.apply(event.getHotelId()));
            room.setFeatures(event.getFeatures());
            return room;
        };
    }
}
