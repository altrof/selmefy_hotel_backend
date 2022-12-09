package tech.selmefy.hotel.service.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.booking.dto.BookingResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingCriteriaRepository;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.room.type.RoomType;

import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private List<Booking> bookingList;
    private Room room1 = new Room(1L, 5F, 1, 1, 5, RoomType.REGULAR, true);
    private Room room2 = new Room(2L, 10F, 1, 2, 2, RoomType.DELUXE, true);


    private Person person = new Person(1L, "1234",
        "Tester",
        "Trickster",
        "Estonia",
        "12234",
        LocalDate.of(2000, 12, 20), new Timestamp(System.currentTimeMillis()));

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
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private TypedQuery<Booking> typedQuery;

    @Mock
    private BookingCriteriaRepository bookingCriteriaRepository;


    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingList = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        bookingList.clear();
    }

    @Test
    void getAllBookings() {

        int numberOfBookings = 2;

        bookingList.add(booking1);
        bookingList.add(booking2);

        // given
        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);

        // when
        List<BookingDTO> result = bookingService.getAllBookings();
        assertEquals(numberOfBookings, result.size());
    }

    @Test
    void getAllBookingsWithParams_NoFiltering() {
        int numberOfBookings = 2;

        bookingList.add(booking1);
        bookingList.add(booking2);

        // given
        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);
        BDDMockito.given(typedQuery.getResultList()).willReturn(bookingList);

        BDDMockito.given(bookingCriteriaRepository.bookingSearchQuery("roomId", "ASC", Optional.empty(), Optional.empty()))
                .willReturn(typedQuery);

        // when
        BookingResponseDTO result = bookingService
            .getAllBookingsWithParams(0,
            10,
            "roomId",
            "ASC", Optional.empty(), Optional.empty());
        assertEquals(numberOfBookings, result.getTotalBookingLength());
    }

    @Test
    void getAllBookingsWithParams_Filtering() {
        int numberOfBookings = 2;

        bookingList.add(booking1);
        bookingList.add(booking2);

        // given
        BDDMockito.given(typedQuery.getResultList()).willReturn(bookingList);

        BDDMockito.given(bookingCriteriaRepository.bookingSearchQuery("roomId", "ASC", Optional.of("comments"), Optional.of("Something")))
                .willReturn(typedQuery);

        // when
        BookingResponseDTO result = bookingService
                .getAllBookingsWithParams(0,
                        10,
                        "roomId",
                        "ASC", Optional.of("comments"), Optional.of("Something"));
        assertEquals(numberOfBookings, result.getTotalBookingLength());
    }

    @Test
    void createNewBooking_throwsAPIRequestException_WhenNoRoomWithId() {

        BookingDTO bookingDTO1 = BookingMapper.INSTANCE.toDTO(booking1);

        BDDMockito.given(roomRepository.findById(5L)).willReturn(Optional.empty());

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                bookingDTO1,
                5L,
                "1234", Optional.empty());
        });

        assertEquals("No such room!", exception.getMessage());
    }

    @Test
    void createNewBooking_throwsAPIRequestException_WhenCheckOutAfterCheckIn() {

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 25))
                .checkOutDate(LocalDate.of(2024, 10, 20))
                .comments("Something something")
                .lateCheckOut(true)
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Check-out has to be after check-in!", exception.getMessage());
    }

    @Test
    void createNewBooking_throwsAPIRequestException_WhenCheckInPast() {

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(1099, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Check-in cannot be in the past!", exception.getMessage());
    }
/*
    @Test
    void createNewBooking_throwsAPIRequestException_WhenRoomNotAvailable() {

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true).personId(1L).personIdentityCode("1234")
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Room is not available at the provided dates!", exception.getMessage());
    }*/
/*
    @Test
    void getBookingById() {
    }

    @Test
    void updateBooking() {
    }*/
}