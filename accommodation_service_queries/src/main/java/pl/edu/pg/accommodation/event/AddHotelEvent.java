package pl.edu.pg.accommodation.event;

import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.model.Hotel;

import java.util.function.Function;

@Data
@Builder
@Jacksonized
public class AddHotelEvent {
    private long id;
    private String name;
    private String country;
    private String city;
    private int stars;
    private String description;
    private String photo;
    private String airport;
    private String food;

    public static Function<AddHotelEvent, Hotel> dtoToEntityMapper() {
        return (event) -> Hotel.builder()
                .id(event.getId())
                .name(event.getName())
                .country(event.getCountry())
                .city(event.getCity())
                .stars(event.getStars())
                .description(event.getDescription())
                .photo(event.getPhoto())
                .airport(event.getAirport())
                .food(event.getFood())
                .rooms(ImmutableSet.of())
                .build();
    }
}
