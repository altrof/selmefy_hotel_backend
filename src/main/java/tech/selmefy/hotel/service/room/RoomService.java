package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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

    public List<Room> getRoomsAvailableBetweenDates(LocalDate fromDate, LocalDate toDate) {
        List<BookingDTO> bookings = bookingService.getAllBookings();

        HashSet<Room> rooms = (HashSet<Room>) roomRepository.findAll();
        for (BookingDTO booking : bookings) {
            for (Room room : rooms) {
                if (Objects.equals(booking.getRoomId(), room.getId())) {
                    if (
                            (booking.getCheckInDate().isAfter(fromDate) && booking.getCheckOutDate().isBefore(toDate))
                        ||
                            (booking.getCheckInDate().isBefore(fromDate) && booking.getCheckOutDate().isAfter(toDate))) {
                        rooms.remove(room);
                    }
                }
            }
        }
        return (List<Room>) rooms;
    }
}
