package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;

    @GetMapping("api/room")
    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> rooms = roomRepository.findAll();
        return rooms;
    }
}
