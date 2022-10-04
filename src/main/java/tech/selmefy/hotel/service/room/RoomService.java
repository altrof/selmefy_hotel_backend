package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return List.of((RoomDTO) rooms.stream().map(RoomMapper.INSTANCE::toDTO));
    }

}
