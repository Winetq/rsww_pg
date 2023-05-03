package pl.edu.pg.accommodation.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

@Repository

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findAllByHotel(HotelEntity hotel);
    Optional<RoomEntity> findRoomEntityByRoomNumber(String roomNumber);
}
