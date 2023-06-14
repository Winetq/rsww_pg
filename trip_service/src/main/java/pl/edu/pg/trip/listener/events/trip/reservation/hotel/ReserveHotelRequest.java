package pl.edu.pg.trip.listener.events.trip.reservation.hotel;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ReserveHotelRequest {
    private Long hotelId;
    private String food;
    private Room room;
    private Long user;
    private String startDate;
    private String endDate;
    @Data
    @Builder
    @Jacksonized
    public static class Room {
        private Integer capacity;
        private String name;
        private String features;
    }
}
