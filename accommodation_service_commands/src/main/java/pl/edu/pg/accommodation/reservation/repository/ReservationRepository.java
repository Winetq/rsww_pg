package pl.edu.pg.accommodation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pg.accommodation.reservation.entity.ReservationEntity;
import pl.edu.pg.accommodation.room.entity.RoomEntity;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    public List<ReservationEntity> findAllByRoom(RoomEntity room);
}
