package pl.edu.pg.accommodation.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.util.StringUtils;
import pl.edu.pg.accommodation.model.Hotel;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@Jacksonized
public class GetHotelsResponseEvent implements Event {

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

    public static Function<Collection<pl.edu.pg.accommodation.model.Hotel>, GetHotelsResponseEvent> entityToDtoMapper() {
        return (entities) -> GetHotelsResponseEvent.builder()
                .hotels(entities.stream()
                        .map(hotel -> Hotel.builder()
                                .id(hotel.getId())
                                .name(hotel.getName())
                                .stars(hotel.getStars())
                                .place(getPlaceSafely(hotel.getCountry(), hotel.getCity()))
                                .photo(hotel.getPhoto())
                                .airport(hotel.getAirport())
                                .build())
                        .collect(Collectors.toList()))
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
