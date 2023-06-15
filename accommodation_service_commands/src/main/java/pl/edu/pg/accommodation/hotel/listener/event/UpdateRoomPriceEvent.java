package pl.edu.pg.accommodation.hotel.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UpdateRoomPriceEvent {
    private Long hotelId;
    private float newPrice;
    private Room room;

    @Data
    @Builder
    @Jacksonized
    public static class Room {
        private int capacity;
        private String name;
        private String features;

    }
}
