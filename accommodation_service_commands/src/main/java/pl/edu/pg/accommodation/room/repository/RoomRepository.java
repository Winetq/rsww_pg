package pl.edu.pg.accommodation.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.List;

@Repository

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Transactional
    @Modifying
    @Query("update RoomEntity r set r.price = ?1")
    int updatePriceBy(float price);
    List<RoomEntity> findAllByHotel(HotelEntity hotel);
}
