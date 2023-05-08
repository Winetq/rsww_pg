package pl.edu.pg.accommodation.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;
import pl.edu.pg.accommodation.room.notifier.RoomEventNotifier;
import pl.edu.pg.accommodation.room.repository.RoomRepository;

import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomEventNotifier notifier;

    @Autowired
    public RoomService(final RoomRepository roomRepository,
                       final RoomEventNotifier notifier) {
        this.roomRepository = roomRepository;
        this.notifier = notifier;
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
}
