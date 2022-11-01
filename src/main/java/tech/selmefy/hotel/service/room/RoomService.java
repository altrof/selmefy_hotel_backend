package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;
    private BookingService bookingService;

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

    public List<RoomDTO> getRoomsAvailableBetweenDates(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isEqual(toDate) || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date must be earlier than end date");
        }
        List<BookingDTO> bookings = bookingService.getAllBookings();
        List<Room> rooms = roomRepository.findAll();
        for (BookingDTO booking : bookings) {
            for (Room room : rooms) {
                if (Objects.equals(booking.getRoomId(), room.getId())) {
                    if (fromDate.isBefore(booking.getCheckInDate())) {
                        if (toDate.isAfter(booking.getCheckInDate())) {
                            rooms.remove(room);
                        }
                    } else if (fromDate.isBefore(booking.getCheckOutDate())) {
                        rooms.remove(room);
                    }
                }
            }
        }
        return rooms.stream().map(RoomMapper.INSTANCE::toDTO).toList();
    }
}
