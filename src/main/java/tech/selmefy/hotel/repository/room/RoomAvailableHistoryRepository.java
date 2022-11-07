package tech.selmefy.hotel.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAvailableHistoryRepository extends JpaRepository<RoomAvailableHistory, Long> {
    List<RoomAvailableHistory> findRoomAvailableHistoriesByRoomId(Long roomId);
}
