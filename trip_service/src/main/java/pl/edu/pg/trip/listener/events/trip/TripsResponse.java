package pl.edu.pg.trip.listener.events.trip;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.listener.events.hotel.HotelDetailsResponse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


@Data
@Builder
@Jacksonized
public class TripsResponse {
    private List<Trip> trips;

    @Data
    @Builder
    @Jacksonized
    public static class Trip {
        private Integer id;
        private Hotel hotel;
        private Double tripPrice;
        private String dateStart;
        private String dateEnd;
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

    public static Function<pl.edu.pg.trip.enity.Trip, Trip> toDtoMapper(final Function<Long, Optional<Transport>> transportAccessor,
                                                                                       final Function<Long, Optional<pl.edu.pg.trip.enity.Hotel>> hotelAccessor) {
        return trip -> {
            final var tripBuilder = Trip.builder();
            final var maybeHotel = hotelAccessor.apply(trip.getHotelId());
            double basePrice = 0L;

            if (maybeHotel.isPresent()) {
                final var hotel = maybeHotel.get();
                tripBuilder.hotel(Hotel.builder().id(hotel.getId())
                        .name(hotel.getName())
                        .stars(hotel.getStars())
                        .place(hotel.getPlace())
                        .photo(hotel.getPhoto())
                        .build());
                basePrice = hotel.getRooms().stream()
                        .mapToDouble(HotelDetailsResponse.Room::getPrice)
                        .min()
                        .getAsDouble();
            }
            tripBuilder.id(trip.getTripId());

            final var startTransport = transportAccessor.apply(trip.getStartFlightId());
            final var endTransport = transportAccessor.apply(trip.getEndFlightId());
            if (startTransport.isPresent()) {
                final var transport = startTransport.get();
                tripBuilder.dateStart(transport.getDepartureDate());
                basePrice = basePrice + transport.getPrice();
            }
            if (endTransport.isPresent()) {
                final var transport = endTransport.get();
                tripBuilder.dateEnd(transport.getDepartureDate());
                basePrice = basePrice + transport.getPrice();
            }

            tripBuilder.tripPrice(basePrice);
            return tripBuilder.build();
        };
    }
}
