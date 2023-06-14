package pl.edu.pg.trip.repository;

import com.mongodb.BasicDBObject;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.pg.trip.storage.MongoDatabaseWrapper;

import java.time.LocalDateTime;

@Repository
public class ReservationRepository {

    private final MongoDatabaseWrapper wrapper;
    private final String COLLECTION_NAME = "trips_reservations";

    @Autowired
    public ReservationRepository(MongoDatabaseWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void deleteOutdatedReservations() {
        final var currentDate = LocalDateTime.now();
        Document filter = new Document();
        filter.append("reserved", new Document("$lt", currentDate));
        filter.append("payed", false);
        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .deleteMany(filter);
    }

    public void save(Reservation reservation) {
        wrapper.getDatabase().getCollection(COLLECTION_NAME, Reservation.class)
                .insertOne(reservation);
    }

    public void markAsPayed(Long reservationId) {
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
    public static class Reservation {
        private Long reservationId;
        private Long startFlightReservation;
        private Long endFlightReservation;
        private Long userId;
        private Long tripId;
        private Long hotelId;
        private Boolean payed;
        private LocalDateTime reserved;
    }
}
