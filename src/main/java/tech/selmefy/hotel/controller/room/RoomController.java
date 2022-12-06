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
import tech.selmefy.hotel.controller.room.dto.RoomResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.service.room.RoomService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Api(tags = "Room")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "rooms")
public class RoomController {
    public final RoomService roomService;
    @GetMapping
    public RoomResponseDTO getAllRooms(
            @RequestParam(name="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(name="orderType", defaultValue = "ASC") String orderType,
            @RequestParam(name = "filterBy") Optional<String> filterBy,
            @RequestParam(name = "filterValue") Optional<String> filterValue) {

        // Ensures that only RoomDTO fields are used for sorting/searching.
        try {
            RoomDTO.class.getDeclaredField(orderBy);
        } catch (NoSuchFieldException e) {
            throw new ApiRequestException("Cannot sort by " + orderBy);
        }

        if (filterBy.isPresent()) {
            try {
                RoomDTO.class.getDeclaredField(filterBy.get());
            } catch (NoSuchFieldException e) {
                throw new ApiRequestException("Cannot filter by " + filterBy);
            }
        }

        // This prevents requesting pages that are too big.
        int maxPageSize = 200;
        if (pageSize > maxPageSize) {
            pageSize = maxPageSize;
        }
        return roomService.getAllRoomsWithParams(pageNumber, pageSize, orderBy, orderType, filterBy, filterValue);
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

    @GetMapping("/public/{from}/{to}")
    public List<RoomDTO> getAllAvailableRooms(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @RequestParam(name = "adults", defaultValue = "1") short adults,
            @RequestParam(name = "children", defaultValue = "0") short children,
            @RequestParam(name = "roomType") Optional<RoomType> roomType) {
        return roomService.getAvailableRooms(from, to, adults, children, roomType);
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