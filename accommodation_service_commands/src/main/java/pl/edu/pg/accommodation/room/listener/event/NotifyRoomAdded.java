package pl.edu.pg.accommodation.room.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.function.Function;
import java.util.function.Supplier;

@Data
@Builder
@Jacksonized
public class NotifyRoomAdded {
    private long id;
    private int capacity;
    private String name;
    private String features;
    private float basePrice;
    private long hotelId;

    public static Function<RoomEntity, NotifyRoomAdded> entityToDtoMapper(final Supplier<Long> hotelIdSupplier) {
        return (room) -> NotifyRoomAdded.builder()
                .id(room.getId())
                .name(room.getName())
                .features(room.getFeatures())
                .basePrice(room.getPrice())
                .hotelId(hotelIdSupplier.get())
                .build();
    }
}
