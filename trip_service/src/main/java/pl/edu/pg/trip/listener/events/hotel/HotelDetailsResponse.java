package pl.edu.pg.trip.listener.events.hotel;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.util.StringUtils;
import pl.edu.pg.trip.enity.Hotel;

import java.util.Set;
import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class HotelDetailsResponse {
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

    public static Function<HotelDetailsResponse, Hotel> dtoToEntityMapper() {
        return response -> Hotel.builder()
                .id(response.getId())
                .place(getPlaceSafely(response.getCountry(), response.getCity()))
                .stars(response.getStars())
                .description(response.getDescription())
                .photo(response.getPhoto())
                .airport(response.getAirport())
                .rooms(response.getRooms())
                .airport(response.getAirport())
                .food(response.getFood())
                .name(response.getName())
                .build();
    }

    private static String getPlaceSafely(final String country, final String city) {
        final var builder = new StringBuilder();
        if (StringUtils.hasText(country)) {
            builder.append(country);
        }
        if (StringUtils.hasText(city)) {
            builder.append(" / ").append(city);
        }
        return builder.toString();
    }
}
