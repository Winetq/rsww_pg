package pl.edu.pg.gateway.hotel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Data
@Builder
@Jacksonized
public class GetHotelDetailResponseEvent {
    private long id;
    private String country;
    private String city;
    private int stars;
    private String description;
    private String photo;
    private Set<Room> rooms;
    private String airport;
    private String food;
    private String name;


    @Data
    @Builder
    @Jacksonized
    public static final class Room {
        private int capacity;
        private String name;
        private String features;
        private float price;

    }
}
