package tech.selmefy.hotel.controller.room;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.room.dto.RoomAvailableHistoryDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.service.room.RoomService;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "Room")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "rooms")
public class RoomController {
    public final RoomService roomService;
    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @ResponseBody
    @GetMapping(params = {"roomType"})
    public List<RoomDTO> getRoomsByType(@RequestParam(name="roomType") String roomType) {
        return roomService.getRoomsByType(roomType);
    }

    @GetMapping("/{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/rooms/{from}/{to}")
    public List<RoomDTO> getAllAvailableRooms(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                              @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return roomService.getRoomsAvailableBetweenDates(from, to);
    }

    @PutMapping("/available/{roomId}")
    public void updateRoomAvailableForBooking(@PathVariable Long roomId, @RequestBody Boolean isAvailable) {
        roomService.updateRoomAvailableForBooking(roomId, isAvailable);
    }

    @GetMapping("/available/history/{roomId}")
    public List<RoomAvailableHistoryDTO> getRoomAvailableHistoryByRoomId(@PathVariable Long roomId) {
        return roomService.getRoomAvailableHistoryByRoomId(roomId);
    }
}