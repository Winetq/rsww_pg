package pl.edu.pg.trip.listener.events.trip.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class PostReservationRequest {
    private Long tripId;
    private String food;
    private Room room;
    private Long user;

    @Data
    @Builder
    @Jacksonized
    public static class Room {
        private Integer capacity;
        private String name;
        private String features;
        private Long key;
    }
}

