package tech.selmefy.hotel.controller.room;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.service.room.RoomService;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "Room")
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

    @GetMapping("/rooms/{from}/{to}")
    public List<RoomDTO> getAllAvailableRooms(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                              @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return roomService.getRoomsAvailableBetweenDates(from, to);
    }
}