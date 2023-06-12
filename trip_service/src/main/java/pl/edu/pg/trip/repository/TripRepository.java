package pl.edu.pg.trip.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.pg.trip.enity.Trip;
import pl.edu.pg.trip.storage.MongoDatabaseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TripRepository {
    private static final Logger log = LoggerFactory.getLogger(TripRepository.class);
    private final MongoDatabaseWrapper mongoDatabaseWrapper;
    private final String TRIPS_COLLECTION = "trips";

    @Autowired
    public TripRepository(final MongoDatabaseWrapper mongoDatabaseWrapper) {
        this.mongoDatabaseWrapper = mongoDatabaseWrapper;
    }

    public Optional<Trip> findTrip(final Long tripId) {
        final var query = new Document("tripId", tripId);
        return Optional.ofNullable(mongoDatabaseWrapper
                .getDatabase()
                .getCollection(TRIPS_COLLECTION, Trip.class)
                .find(query)
                .first()
        );
    }

    public List<Trip> findAllTrips() {
        final var trips = new ArrayList<Trip>();
        mongoDatabaseWrapper.getDatabase()
                .getCollection(TRIPS_COLLECTION, Trip.class)
                .find()
                .into(trips);
        return trips;
    }

    public Trip save(Trip trip) {
        if (trip.getTripId() == null) {
            final var uuid = UUID.randomUUID().hashCode();
            trip.setTripId(uuid);
        }
        try {
            mongoDatabaseWrapper.getDatabase()
                    .getCollection(TRIPS_COLLECTION, Trip.class)
                    .insertOne(trip);
        } catch (MongoWriteException ex) {
            log.error("Exception during writing to the database.", ex);
        }
        return trip;
    }

    public void delete(Long id) {
        final var query = new BasicDBObject();
        query.put("tripId", id);
        mongoDatabaseWrapper.getDatabase()
                .getCollection(TRIPS_COLLECTION, Trip.class)
                .deleteOne(query);
    }
}
