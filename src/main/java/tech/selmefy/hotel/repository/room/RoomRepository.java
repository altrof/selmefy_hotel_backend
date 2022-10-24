package tech.selmefy.hotel.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
