package pl.edu.pg.trip.listener.events.trip;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.trip.enity.Hotel;
import pl.edu.pg.trip.enity.Transport;
import pl.edu.pg.trip.enity.Trip;

import java.time.format.DateTimeFormatter;
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
        private Long id;
        private Hotel hotel;
        private Float tripPrice;
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

    public static Function<List<pl.edu.pg.trip.enity.Trip>, TripsResponse> toDtoMapper(final Function<Long, Optional<Transport>> transportAccessor,
                                                                                       final Function<Long, Optional<pl.edu.pg.trip.enity.Hotel>> hotelAccessor,
                                                                                       final Supplier<Float> priceSupplier) {
        return trips -> TripsResponse.builder().trips(trips.stream()
                        .map(trip -> {
                            final var tripBuilder = Trip.builder();
                            final var maybeHotel = hotelAccessor.apply(trip.getHotelId());
                            if (maybeHotel.isPresent()) {
                                final var hotel = maybeHotel.get();
                                tripBuilder.hotel(Hotel.builder().id(hotel.getId())
                                                .name(hotel.getName())
                                                .stars(hotel.getStars())
                                                .place(hotel.getPlace())
                                                .photo(hotel.getPhoto())
                                        .build());
                            }

                            tripBuilder.id(trip.getTripId())
                                    .tripPrice(priceSupplier.get());

                            final var startTransport = transportAccessor.apply(trip.getStartFlightId());
                            final var endTransport = transportAccessor.apply(trip.getEndFlightId());
                            startTransport.ifPresent(transport -> tripBuilder.dateStart(transport.getDepartureDate()));
                            endTransport.ifPresent(transport -> tripBuilder.dateEnd(transport.getDepartureDate()));


                            return tripBuilder.build();
                        })
                .toList()).build();
    }
}
