package pl.edu.pg.accommodation.hotel.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.event.Event;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@Builder
@Jacksonized
public class AddHotelEvent implements Event {

    private String name;
    private String city;
    private String country;
    private int stars;
    private String description;
    private String photo;
    private Set<Room> rooms;
    private String airport;
    private String food;

    public static Function<AddHotelEvent, HotelEntity> toEntityMapper() {
        return (event) -> HotelEntity.builder()
                .name(event.getName())
                .city(event.getCity())
                .country(event.getCountry())
                .stars(event.getStars())
                .description(event.getDescription())
                .photo(event.getPhoto())
                .airport(event.getAirport())
                .food(event.getFood())
                .build();
    }

    @Data
    @Builder
    @Jacksonized
    public static class Room {
        private int capacity;
        private String name;
        private String features;
        private int numberOfRooms;
        private float basePrice;

        public static Function<Room, RoomEntity> toEntityMapper(final Supplier<HotelEntity> hotelEntitySupplier) {
            return (room) -> RoomEntity.builder()
                    .capacity(room.getCapacity())
                    .name(room.getName())
                    .features(room.getFeatures())
                    .price(room.getBasePrice())
                    .hotel(hotelEntitySupplier.get())
                    .build();
        }
    }
}
