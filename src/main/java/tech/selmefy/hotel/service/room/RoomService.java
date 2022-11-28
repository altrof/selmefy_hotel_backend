package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.room.dto.RoomAvailableHistoryDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.RoomAvailableHistoryMapper;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomAvailableHistory;
import tech.selmefy.hotel.repository.room.RoomAvailableHistoryRepository;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;
    private BookingService bookingService;
    public final RoomAvailableHistoryRepository roomAvailableHistoryRepository;

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

    /**
     * Returns a list of RoomDTO-s that are available for the whole duration between the provided dates.
     * @param fromDate start date of the search
     * @param toDate end date of the search
     * @return List of RoomDTO-s that available between the two provided dates.
     */
    public List<RoomDTO> getAvailableRooms(
        LocalDate fromDate,
        LocalDate toDate,
        short adults,
        short children,
        Optional<RoomType> roomType) {

        if (fromDate.isEqual(toDate) || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date must be earlier than end date");
        }

        if (adults == 0) {
            throw new IllegalArgumentException("At least one adult needs to be present");
        }
        Map<Long, Room> rooms = roomRepository.findAll().stream()
            .filter(room -> doesRoomTypeMatch(room, roomType))
            .filter(room -> room.getNumberOfBeds() >= adults + children)
            .collect(Collectors.toMap(Room::getId, Function.identity()));
        List<BookingDTO> bookings = bookingService.getAllBookings();

        for (BookingDTO booking : bookings) {
            Optional<Room> room = Optional.ofNullable(rooms.get(booking.getRoomId()));
            if (room.isPresent() && Boolean.TRUE.equals(room.get().getRoomAvailableForBooking())) {
                if (fromDate.isBefore(booking.getCheckInDate())) {
                    if (toDate.isAfter(booking.getCheckInDate())) {
                        rooms.remove(booking.getRoomId());
                    }
                } else if (fromDate.isBefore(booking.getCheckOutDate())) {
                    rooms.remove(booking.getRoomId());
                }
            }
        }

        List<RoomDTO> output = new ArrayList<>();
        rooms.forEach((key, room) -> transformToRoomDTOAndPutToList(room, output));

        return output;
    }
    private void transformToRoomDTOAndPutToList(Room room, List<RoomDTO> roomDTOList) {
        RoomDTO roomDTO = RoomMapper.INSTANCE.toDTO(room);
        roomDTOList.add(roomDTO);
    }

    private static boolean doesRoomTypeMatch(Room room, Optional<RoomType> requiredRoomType) {
        if (requiredRoomType.isPresent()) {
            if (room.getRoomType() != requiredRoomType.get()) {
                return false;
            }
        }
        return true;
    }

    public List<RoomAvailableHistoryDTO> getRoomAvailableHistoryByRoomId(Long roomId) {
        return roomAvailableHistoryRepository.findRoomAvailableHistoriesByRoomId(roomId).stream()
            .map(RoomAvailableHistoryMapper.INSTANCE::toDTO)
            .toList();
    }

    public void updateRoomAvailableForBooking(Long roomId, Boolean isBookingAvailable) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        if(room.getRoomAvailableForBooking().equals(isBookingAvailable)) {
            throw new ApiRequestException("This value is already defined for this field.");
        }
        room.setRoomAvailableForBooking(isBookingAvailable);
        roomRepository.save(room);

        RoomAvailableHistory roomAvailableHistory = new RoomAvailableHistory();
        roomAvailableHistory.setRoomId(roomId);
        roomAvailableHistory.setRoomAvailableForBooking(isBookingAvailable);
        roomAvailableHistory.setCreatedDtime(Timestamp.valueOf(LocalDateTime.now()));
        roomAvailableHistoryRepository.save(roomAvailableHistory);
    }
}
