package pl.edu.pg.accommodation.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.model.Room;

import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class AddRoomEvent {
    private long id;
    private int capacity;
    private String name;
    private String features;
    private float basePrice;
    private long hotelId;
    public static Function<AddRoomEvent, Room> dtoToEntityMapper() {
        return (event) -> Room.builder()
                .id(event.getId())
                .name(event.getName())
                .features(event.getFeatures())
                .price(event.getBasePrice())
                .build();
    }
}
