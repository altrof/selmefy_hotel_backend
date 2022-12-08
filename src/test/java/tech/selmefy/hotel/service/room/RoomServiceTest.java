package tech.selmefy.hotel.service.room;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomResponseDTO;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomCriteriaRepository;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private List<Room> roomList;
    private List<BookingDTO> bookingDTOList;
    private Room room1 = new Room(1L, 5F, 1, 1, 5, RoomType.REGULAR, true);
    private Room room2 = new Room(2L, 10F, 1, 2, 2, RoomType.DELUXE, true);
    private Room room3 = new Room(3L, 10F, 1, 2, 2, RoomType.ECONOMY, false);
    private Room room4 = new Room(4L, 10F, 1, 2, 2, RoomType.REGULAR, true);

    private List<Long> roomIdList = Arrays.asList(new Long[]{1L, 2L, 3L, 4L});
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomCriteriaRepository roomCriteriaRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private TypedQuery<Room> typedQuery;

    @Spy
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomList = new ArrayList<>();
        bookingDTOList = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        roomList.clear();
        bookingDTOList.clear();
    }

    @Test
    void getAllRoomsWithParamsNoFilterValue() {

        // given
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);

        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);
        BDDMockito.given(typedQuery.getResultList()).willReturn(roomList);

        BDDMockito.given(roomCriteriaRepository.roomSearchQuery("id", "ASC", Optional.empty(), Optional.empty()))
            .willReturn(typedQuery);

        // when

        RoomResponseDTO result = roomService.getAllRoomsWithParams(0,10, "id", "ASC", Optional.empty(), Optional.empty());

        // then
        assertEquals(roomList.size(), result.getTotalRoomsLength());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result.getRooms()) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }


    @Test
    void getAllRoomsWithParamsFilterValueSet() {

        // given
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);

        BDDMockito.given(typedQuery.getResultList()).willReturn(roomList);

        BDDMockito.given(roomCriteriaRepository.roomSearchQuery("id", "ASC", Optional.of("size"), Optional.of("10")))
                .willReturn(typedQuery);

        // when

        RoomResponseDTO result = roomService.getAllRoomsWithParams(0,10, "id", "ASC", Optional.of("size"), Optional.of("10"));

        // then
        assertEquals(roomList.size(), result.getTotalRoomsLength());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result.getRooms()) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }


    @Test
    void getRoomsByTypeRegular() {

        // given
        roomList.add(room1);
        roomList.add(room4);
        BDDMockito.given(roomRepository.findRoomsByRoomType(RoomType.REGULAR)).willReturn(roomList);

        // then
        List<RoomDTO> result = roomService.getRoomsByType("REGULAR");

        // then
        assertEquals(roomList.size(), result.size());

        // Confirming all roomDTO-s have roomType "REGULAR".
        for (RoomDTO roomDTO : result) {
            assertTrue(roomDTO.getRoomType().equals("REGULAR"));
        }
    }

    @Test
    void getAvailableRoomsNoRoomTypeNoBookings() {

        // All rooms are available except for where getRoomAvailableForBooking is set false.
        int numberOfAvailableRooms = 3;
        // given
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);
        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);

        BDDMockito.given(bookingService.getAllBookings()).willReturn(new ArrayList<>());
        // then
        List<RoomDTO> result = roomService.getAvailableRooms(
            LocalDate.of(2024, 10, 20),
            LocalDate.of(2024, 10, 25),
                (short) 1, (short) 0, Optional.empty());

        // then
        assertEquals(numberOfAvailableRooms, result.size());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }

    @Test
    void getAvailableRoomsRegularNoBookings() {

        // All Regular rooms are available except for where getRoomAvailableForBooking is set false.
        int numberOfRegularRooms = 2;
        // given
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);
        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);

        BDDMockito.given(bookingService.getAllBookings()).willReturn(new ArrayList<>());
        // then
        List<RoomDTO> result = roomService.getAvailableRooms(
                LocalDate.of(2024, 10, 20),
                LocalDate.of(2024, 10, 25),
                (short) 1, (short) 0, Optional.of(RoomType.REGULAR));

        // then
        assertEquals(numberOfRegularRooms, result.size());

        // Confirming all roomDTO-s have roomType "REGULAR".
        for (RoomDTO roomDTO : result) {
            assertTrue(roomDTO.getRoomType().equals("REGULAR"));
        }
    }

    @Test
    void getAvailableRoomsNoRoomTypeBookedExactDates() {

        // All rooms are available except for where getRoomAvailableForBooking is set false
        // and overlapping booking exists.
        int numberOfFreeRooms = 1;
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);

        Booking booking1 = Booking.builder()
            .id(1L)
            .roomId(1L)
            .room(room1)
            .price(50)
            .checkInDate(LocalDate.of(2024, 10, 20))
            .checkOutDate(LocalDate.of(2024, 10, 25))
            .comments("Something something")
            .lateCheckOut(true)
            .build();

        Booking booking2 = Booking.builder()
                .id(2L)
                .roomId(2L)
                .room(room2)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(false)
                .build();

        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking1));
        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking2));
        // given
        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);

        BDDMockito.given(bookingService.getAllBookings()).willReturn(bookingDTOList);

        // then
        List<RoomDTO> result = roomService.getAvailableRooms(
                LocalDate.of(2024, 10, 20),
                LocalDate.of(2024, 10, 25),
                (short) 1, (short) 0, Optional.empty());

        // then
        assertEquals(numberOfFreeRooms, result.size());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }

    @Test
    void getAvailableRoomsNoRoomTypeBookedPartialDates() {

        // All rooms are available except for where getRoomAvailableForBooking is set false
        // and overlapping booking exists.
        int numberOfFreeRooms = 1;
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);

        Booking booking1 = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 19))
                .checkOutDate(LocalDate.of(2024, 10, 21))
                .comments("Something something")
                .lateCheckOut(true)
                .build();

        Booking booking2 = Booking.builder()
                .id(2L)
                .roomId(2L)
                .room(room2)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 24))
                .checkOutDate(LocalDate.of(2024, 10, 26))
                .comments("Something something")
                .lateCheckOut(false)
                .build();

        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking1));
        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking2));
        // given
        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);

        BDDMockito.given(bookingService.getAllBookings()).willReturn(bookingDTOList);
        // then
        List<RoomDTO> result = roomService.getAvailableRooms(
                LocalDate.of(2024, 10, 20),
                LocalDate.of(2024, 10, 25),
                (short) 1, (short) 0, Optional.empty());

        // then
        assertEquals(numberOfFreeRooms, result.size());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }
}
