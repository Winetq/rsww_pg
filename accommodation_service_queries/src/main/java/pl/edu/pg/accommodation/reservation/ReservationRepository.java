package pl.edu.pg.accommodation.reservation;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.pg.accommodation.model.Reservation;
import pl.edu.pg.accommodation.storage.MongoDatabaseWrapper;

@Repository
public class ReservationRepository {
    private static final String RESERVATIONS_COLLECTION = "reservations";
    private static final Logger log = LoggerFactory.getLogger(ReservationRepository.class);
    private final MongoDatabaseWrapper mongoDb;

    @Autowired
    public ReservationRepository(final MongoDatabaseWrapper mongoDb) {
        this.mongoDb = mongoDb;
    }

    public void addReservation(final Reservation reservation) {
        try {
            mongoDb.getDatabase()
                    .getCollection(RESERVATIONS_COLLECTION, Reservation.class)
                    .insertOne(reservation);
        } catch (MongoWriteException exception) {
            log.error("Error when adding reservation.", exception);
        }

    }

    public void removeReservation(Long reservationId) {
        var query = new BasicDBObject();
        query.put("reservationId", reservationId);
        try {
            mongoDb.getDatabase()
                    .getCollection(RESERVATIONS_COLLECTION, Reservation.class)
                    .deleteOne(query);
        } catch (MongoWriteException exception) {
            log.error("Error when adding reservation.", exception);
        }
    }
}
