package tech.selmefy.hotel.controller.room;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.service.room.RoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {
    public final RoomService roomService;
    @GetMapping("/rooms")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{roomType}")
    public List<RoomDTO> getRoomsByType(@PathVariable String roomType) {
        return roomService.getRoomsByType(roomType);
    }

    @GetMapping("/room/{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }
}