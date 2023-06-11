package pl.edu.pg.trip.listener.events.trip;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.trip.enity.Trip;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
@Builder
@Jacksonized
public class TripDetailsResponse {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Hotel hotel;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Transport transport;

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
        private Long travelTime;
        private Long placesCount;
        private Long placesOccupied;
    }

    public static Function<Trip, TripDetailsResponse> toDtoMapper(Supplier<Optional<pl.edu.pg.trip.enity.Hotel>> hotelSupplier,
                                                                  Supplier<Optional<pl.edu.pg.trip.enity.Transport>> transportSupplier) {
        return trip -> {
            final var maybeHotel = hotelSupplier.get();
            final var maybeTransport = transportSupplier.get();
            final var tripDetailsBuilder = TripDetailsResponse.builder();
            if (maybeTransport.isPresent()) {
                final var transport = maybeTransport.get();
                tripDetailsBuilder.transport(Transport.builder()
                        .departureAirport(transport.getDepartureAirport())
                        .arrivalAirport(transport.getArrivalAirport())
                        .departureDateTime(transport.getDepartureDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                        .arrivalDate(transport.getArrivalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                        .travelTime(transport.getTravelTime())
                        .placesCount(transport.getPlacesCount())
                        .placesOccupied(transport.getPlacesOccupied())
                        .build());
            }

            if (maybeHotel.isPresent()) {
                final var hotel = maybeHotel.get();
                tripDetailsBuilder.hotel(Hotel.builder()
                        .id(hotel.getId())
                        .name(hotel.getName())
                        .country(extractCountry(hotel.getPlace()))
                        .city(extractCity(hotel.getPlace()))
                        .description(hotel.getDescription())
                        .stars(hotel.getStars())
                        .place(hotel.getPlace())
                        .photo(hotel.getPhoto())
                        .airport(hotel.getAirport())
                        .food(hotel.getFood())
                        .rooms(hotel.getRooms().stream().map(room -> Hotel.Room.builder()
                                .capacity(room.getCapacity())
                                .name(room.getName())
                                .features(room.getFeatures())
                                .build()).collect(Collectors.toList()))
                        .build());
            }
            return tripDetailsBuilder.build();
        };
    }

    private static String extractCity(String place) {
        final var tokens = place.split("/");
        if (tokens.length > 1) {
            return tokens[1].trim();
        } else return "";
    }

    private static String extractCountry(String place) {
        final var tokens = place.split("/");
        if (tokens.length > 0) {
            return tokens[0].trim();
        } else return "";
    }
}
