package pl.edu.pg.accommodation.hotel.service;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.hotel.repository.HotelRepository;
import pl.edu.pg.accommodation.model.Hotel;
import pl.edu.pg.accommodation.model.Room;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(final HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAllHotels();
    }

    public Optional<Hotel> getHotel(final Long id) {
        return hotelRepository.findHotel(id);
    }

    public Hotel addHotel(final Hotel hotel) {
        return hotelRepository.addHotel(hotel);
    }

    public Room addRoomInHotel(final long hotelId, final Room room) {
        return hotelRepository.addRoomInHotel(hotelId, room);
    }
    public void updateRoomPrice(final Room room, final long hotelId) {
        hotelRepository.updateRoomPrice(room, hotelId);
    }

    public Set<String> getDestinations() {
        return hotelRepository.getDestinations();
    }
}
