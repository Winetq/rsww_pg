package pl.edu.pg.accommodation.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.model.Hotel;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@Jacksonized
public class GetHotelDetailResponseEvent implements Event {
    private long id;
    private String country;
    private String city;
    private int stars;
    private String description;
    private String photo;
    private Set<Room> rooms;
    private String airport;
    private String food;

    @Data
    @Builder
    @Jacksonized
    public static final class Room {
        private int capacity;
        private String name;
        private String features;
    }

    public static Function<Hotel, GetHotelDetailResponseEvent> entityToDtoMapper() {
        return (entity) -> GetHotelDetailResponseEvent.builder()
                .id(entity.getId())
                .country(entity.getCountry())
                .city(entity.getCity())
                .stars(entity.getStars())
                .description(entity.getDescription())
                .photo(entity.getPhoto())
                .rooms(entity.getRooms().stream().map(room -> Room.builder()
                        .capacity(room.getCapacity())
                        .name(room.getName())
                        .features(room.getFeatures())
                        .build()).collect(Collectors.toSet()))
                .airport(entity.getAirport())
                .food(entity.getFood())
                .build();
    }
}
