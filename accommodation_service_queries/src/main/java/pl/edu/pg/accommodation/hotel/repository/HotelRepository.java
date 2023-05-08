package pl.edu.pg.accommodation.hotel.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.pg.accommodation.model.Hotel;
import pl.edu.pg.accommodation.model.Room;
import pl.edu.pg.accommodation.storage.MongoDatabaseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class HotelRepository {
    private static final Logger log = LoggerFactory.getLogger(HotelRepository.class);
    private static final String HOTEL_COLLECTION = "hotels";
    private final MongoDatabaseWrapper mongoDb;

    @Autowired
    public HotelRepository(final MongoDatabaseWrapper mongoDb) {
        this.mongoDb = mongoDb;
    }

    public List<Hotel> findAllHotels() {
        final var allHotels = new ArrayList<Hotel>();
        hotelsCollection().find().into(allHotels);
        return allHotels;
    }

    public Hotel addHotel(final Hotel hotel) {
        try {
            hotelsCollection().insertOne(hotel);
        } catch (MongoWriteException exception) {
            log.error("Error when adding hotel.", exception);
        }
        return hotel;
    }

    public Room addRoomInHotel(final long hotelId, final Room room) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", hotelId);
        final var maybeHotel = Optional.ofNullable(hotelsCollection().find(query).first());
        maybeHotel.ifPresentOrElse((hotel) -> {
            hotelsCollection().updateOne(
                    Filters.eq("_id", hotelId),
                    Updates.addToSet("rooms", room)
            );
        }, () -> {
            log.error("No hotel with id: {}. Cannot add room to not existing hotel.", hotelId);
            throw new NoSuchElementException("No hotel found.");
        });
        return room;
    }

    private MongoCollection<Hotel> hotelsCollection() {
        return mongoDb.getDatabase().getCollection(HOTEL_COLLECTION, Hotel.class);
    }
}
