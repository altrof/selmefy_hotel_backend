package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : rooms) {
            RoomDTO roomDTO = RoomMapper.INSTANCE.toDTO(room);
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }

    public List<RoomDTO> getRoomsByType(String roomType) {
        return roomRepository.findRoomsByRoomType(RoomType.valueOf(roomType.toUpperCase())).stream()
                .map(RoomMapper.INSTANCE::toDTO)
                .toList();
    }

    public RoomDTO getRoomById(Long id) {
        return roomRepository.findById(id).map(RoomMapper.INSTANCE::toDTO).orElseThrow();
    }
}
