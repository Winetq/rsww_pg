package pl.edu.pg.trip.repository;

import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.pg.trip.storage.MongoDatabaseWrapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Repository
public class ReservationRepository {

    private final MongoDatabaseWrapper wrapper;
    private final String COLLECTION_NAME = "trips_reservations";

    @Autowired
    public ReservationRepository(MongoDatabaseWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public Set<Reservation> deleteOutdatedReservations() {
        final var currentDate = LocalDateTime.now();
        final var reservations = new HashSet<Reservation>();
        Document filter = new Document();
        filter.append("reserved", new Document("$lt", currentDate));
        filter.append("payed", false);


        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .find(filter)
                .into(reservations);
        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .deleteMany(filter);
        return reservations;
    }

    public void save(Reservation reservation) {
        if (reservation.getReservationId() == null) {
            final var uuid = UUID.randomUUID().hashCode();
            reservation.setReservationId(uuid);
        }
        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .insertOne(reservation);
    }

    public Set<Reservation> getUserReservations(Long userId) {
        final var results = new HashSet<Reservation>();
        final var query = new Document("userId", userId);

        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .find(query)
                .into(results);
        return results;
    }

    public void markAsPayed(Integer reservationId) {
        final var query = new BasicDBObject();
        query.put("reservationId", reservationId);
        Document update = new Document("$set", new Document("payed", true));
        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .updateOne(query, update);
    }

    public void deleteReservation(Long reservationId) {
        final var query = new BasicDBObject();
        query.put("reservationId", reservationId);
        wrapper.getDatabase()
                .getCollection(COLLECTION_NAME, Reservation.class)
                .deleteOne(query);
    }

    @Data
    @Builder
    @Jacksonized
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reservation {
        private Integer reservationId;
        private Long startFlightReservation;
        private Long endFlightReservation;
        private Long startFlightId;
        private Long endFlightId;
        private Long userId;
        private Long hotelReservation;
        private Integer tripId;
        private Long hotelId;
        private Boolean payed;
        private LocalDateTime reserved;
        private Double price;
    }
}
