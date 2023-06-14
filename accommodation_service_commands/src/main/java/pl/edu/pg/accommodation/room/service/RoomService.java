package pl.edu.pg.accommodation.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.hotel.service.HotelService;
import pl.edu.pg.accommodation.room.entity.RoomEntity;
import pl.edu.pg.accommodation.room.notifier.RoomEventNotifier;
import pl.edu.pg.accommodation.room.repository.RoomRepository;

import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomEventNotifier notifier;
    private final HotelService hotelService;

    @Autowired
    public RoomService(final RoomRepository roomRepository,
                       final RoomEventNotifier notifier,
                       final HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.notifier = notifier;
        this.hotelService = hotelService;
    }

    public RoomEntity addRoom(final RoomEntity room) {
        var newRoom = roomRepository.save(room);
        notifier.notifyAddRoom(newRoom);
        return newRoom;
    }

    public void deleteRoom(final RoomEntity room) {
        roomRepository.delete(room);
    }

    public Iterable<RoomEntity> findAllRooms() {
        return roomRepository.findAll();
    }

    public Iterable<RoomEntity> findAllRoomsInHotel(final HotelEntity hotel) {
        return roomRepository.findAllByHotel(hotel);
    }

    public Optional<RoomEntity> findRoom(final Long roomId) {
        return roomRepository.findById(roomId);
    }

    public Optional<RoomEntity> findMatchingRoom(Long hotelId, Integer capacity, String name, String features) {
        final var hotel = hotelService.findHotelById(hotelId);
        if (hotel.isEmpty()) {
            return Optional.empty();
        }
        return roomRepository.findAllByHotel(hotel.get()).stream()
                .filter(room -> room.getCapacity() >= capacity)
                .filter(room -> room.getName().equals(name))
                .filter(room -> room.getFeatures().equals(features))
                .findFirst();
    }
}
