package pl.edu.pg.gateway.trip.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class TripDetailsResponse {
    private Hotel hotel;
    private Transport transport;
    private Transport returnTransport;

    @Data
    @Builder
    @Jacksonized
    public static class Hotel {
        private Long id;
        private String name;
        private String country;
        private String city;
        private String description;
        private int stars;
        private String place;
        private String photo;
        private List<Room> rooms;
        private String airport;
        private String food;

        @Data
        @Builder
        @Jacksonized
        public static class Room {
            private int capacity;
            private String name;
            private String features;
        }
    }

    @Data
    @Builder
    @Jacksonized
    public static class Transport {
        private String departureAirport;
        private String arrivalAirport;
        private String departureDateTime;
        private String arrivalDate;
        private int travelTime;
        private int placesCount;
        private int placesOccupied;
    }
}
