package pl.edu.pg.trip.listener.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class GetHotelsResponse {

    private List<Hotel> hotels;

    @Data
    @Builder
    @Jacksonized
    public static class Hotel {
        private Long id;
        private String name;
        private int stars;
        private String place;
        private String photo;
        private String airport;
    }

    public static Function<GetHotelsResponse, List<pl.edu.pg.trip.enity.Hotel>> dtoToEntityMapper() {
        return response -> response.getHotels().stream()
                .map(hotelResponse -> pl.edu.pg.trip.enity.Hotel.builder()
                        .place(hotelResponse.getPlace())
                        .id(hotelResponse.getId())
                        .stars(hotelResponse.getStars())
                        .photo(hotelResponse.getPhoto())
                        .airport(hotelResponse.getAirport())
                        .build())
                .collect(Collectors.toList());
    }
}
