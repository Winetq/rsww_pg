package pl.edu.pg.trip.listener.events;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@Jacksonized
public class TripsResponse {
    private List<Trip> trips;

    @Data
    @Builder
    @Jacksonized
    public static class Trip {
        private Long id;
        private Hotel hotel;
        private Float tripPrice;
        private LocalDate dateStart;
        private LocalDate dateEnd;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Hotel {
        private Long id;
        private String name;
        private int stars;
        private String place;
        private String photo;
    }
}
