package tech.selmefy.hotel.controller.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.booking.dto.BookingResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private List<BookingDTO> bookingDTOList;
    private Room room1 = new Room(1L, 5F, 1, 1, 5, RoomType.REGULAR, true);
    private Room room2 = new Room(2L, 10F, 1, 2, 2, RoomType.DELUXE, true);

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

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingDTOList = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        bookingDTOList.clear();
    }

    @Test
    void getAllBookingsWithParams_defaultSettings() {

        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking1));
        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking2));
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO(bookingDTOList, bookingDTOList.size());

        // given
        BDDMockito.given(bookingService.getAllBookingsWithParams(
        0,
         10,
         "price",
         "ASC",
         Optional.empty(),
         Optional.empty())).willReturn(bookingResponseDTO);

        // when
        BookingResponseDTO result = bookingController.getAllBookingsWithParams(
        0,
        10,
        "price",
        "ASC",
        Optional.empty(),
        Optional.empty());

        assertEquals(bookingDTOList.size(), result.getTotalBookingLength());
    }

    @Test
    void getAllBookingsWithParams_paginationOverMaxPageSize() {

        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking1));
        bookingDTOList.add(BookingMapper.INSTANCE.toDTO(booking2));
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO(bookingDTOList, bookingDTOList.size());

        // given
        BDDMockito.given(bookingService.getAllBookingsWithParams(
                0,
                200,
                "price",
                "ASC",
                Optional.empty(),
                Optional.empty())).willReturn(bookingResponseDTO);

        // when
        BookingResponseDTO result = bookingController.getAllBookingsWithParams(
                0,
                10000,
                "price",
                "ASC",
                Optional.empty(),
                Optional.empty());

        assertEquals(bookingDTOList.size(), result.getTotalBookingLength());
    }

    @Test
    void getAllBookingsWithParams_throwsApiRequestException_whenInvalidOrderBy() {

        // when
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingController.getAllBookingsWithParams(
                    0,
                    10,
                    "firstName",
                    "ASC",
                    Optional.empty(),
                    Optional.empty());
        });

        assertEquals("Cannot sort by firstName", exception.getMessage());    }

    @Test
    void getAllBookingsWithParams_throwsApiRequestException_whenInvalidFilterBy() {

        // when
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingController.getAllBookingsWithParams(
                    0,
                    10,
                    "price",
                    "ASC",
                    Optional.of("firstName"),
                    Optional.empty());
        });

        assertEquals("Cannot filter by firstName", exception.getMessage());    }

    @Test
    void getBookingById_success() {

        // given
        BDDMockito.given(bookingService.getBookingById(1L)).willReturn(BookingMapper.INSTANCE.toDTO(booking1));

        BookingDTO result = bookingController.getBookingById(1L);

        assertEquals(booking1.getCheckInDate(), result.getCheckInDate());
        assertEquals(booking1.getCheckOutDate(), result.getCheckOutDate());
        assertEquals(booking1.isLateCheckOut(), result.isLateCheckOut());
        assertEquals(booking1.getPrice(), result.getPrice());
    }


    @Test
    void updateBooking() {

        BookingDTO upDatedBookingDTO = BookingDTO.builder()
                .roomId(1L)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 21))
                .checkOutDate(LocalDate.of(2024, 10, 26))
                .comments("Updated booking")
                .lateCheckOut(true)
                .build();

        // given
        BDDMockito.given(bookingService.updateBooking(1L, upDatedBookingDTO)).willReturn(upDatedBookingDTO);

        // when
        ResponseEntity<BookingDTO> result = bookingController.updateBooking(1L, upDatedBookingDTO);

        assertEquals(upDatedBookingDTO.getCheckInDate(), result.getBody().getCheckInDate());
        assertEquals(upDatedBookingDTO.getCheckOutDate(), result.getBody().getCheckOutDate());
        assertEquals(upDatedBookingDTO.isLateCheckOut(), result.getBody().isLateCheckOut());
        assertEquals(upDatedBookingDTO.getPrice(), result.getBody().getPrice());
    }
}