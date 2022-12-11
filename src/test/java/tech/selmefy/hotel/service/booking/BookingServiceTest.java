package tech.selmefy.hotel.service.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.booking.dto.BookingResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingCriteriaRepository;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.personinbooking.PersonInBookingRepository;
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
import static org.mockito.ArgumentMatchers.any;

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

    private Person otherPerson1 = new Person(1L, "1235",
            "Tester2",
            "Trickster2",
            "Estonia",
            "12234",
            LocalDate.of(2000, 12, 20), new Timestamp(System.currentTimeMillis()));


    private Person otherPerson2 = new Person(1L, "1236",
            "Tester3",
            "Trickster3",
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
    private PersonRepository personRepository;

    @Mock
    private PersonInBookingRepository personInBookingRepository;

    @Mock
    private BookingMapper bookingMapper;

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
    void getAllBookings_returnsCorrectListOfBookings() {

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
    void getAllBookingsWithParams_noFiltering() {
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
    void getAllBookingsWithParams_returnsFilteredList_whenProvidedParamsAreExist() {
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
    void createNewBooking_throwsAPIRequestException_whenNoRoomWithId() {

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
    void createNewBooking_throwsAPIRequestException_whenCheckOutAfterCheckIn() {

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
    void createNewBooking_throwsAPIRequestException_whenCheckInPast() {

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

    @Test
    void createNewBooking_throwsAPIRequestException_roomNotAvailable_setFalse() {

        Room unavailableRoom = new Room(3L,
        5F,
        1,
        1,
        5,
        RoomType.REGULAR,
        false);


        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(3L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(roomRepository.findById(3L)).willReturn(Optional.of(unavailableRoom));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    3L,
                    "1234", Optional.empty());
        });

        assertEquals("Room is not available at the provided dates!", exception.getMessage());
    }

    @Test
    void createNewBooking_throwsAPIRequestException_roomNotAvailable_anotherBookingExactMatch() {

        bookingList.add(booking1);
        bookingList.add(booking2);

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);
        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Room is not available at the provided dates!", exception.getMessage());
    }


    @Test
    void createNewBooking_throwsAPIRequestException_roomNotAvailable_anotherBookingPartialMatchStart() {

        bookingList.add(booking1);
        bookingList.add(booking2);

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 17))
                .checkOutDate(LocalDate.of(2024, 10, 21))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);
        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Room is not available at the provided dates!", exception.getMessage());
    }

    @Test
    void createNewBooking_throwsAPIRequestException_roomNotAvailable_anotherBookingPartialMatchEnd() {

        bookingList.add(booking1);
        bookingList.add(booking2);

        Booking invalidBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 24))
                .checkOutDate(LocalDate.of(2024, 10, 26))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();
        BookingDTO invalidBookingDTO = BookingMapper.INSTANCE.toDTO(invalidBooking);

        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);
        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookingService.createNewBooking(
                    invalidBookingDTO,
                    1L,
                    "1234", Optional.empty());
        });

        assertEquals("Room is not available at the provided dates!", exception.getMessage());
    }

    @Test
    void createNewBooking_successOnePerson() {
        Booking validBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();

        BookingDTO validBookingDTO = BookingMapper.INSTANCE.toDTO(validBooking);

        BDDMockito.given(personRepository.findPersonByIdentityCode("1234")).willReturn(Optional.of(person));
        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));
        BDDMockito.doNothing().when(personInBookingRepository)
            .addPersonInBooking(validBooking.getId(),
            validBooking.getPersonId(),
            validBooking.getPersonIdentityCode());

        BDDMockito.lenient().when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);


        assertDoesNotThrow(() -> {
            bookingService.createNewBooking(
                    validBookingDTO,
                    1L,
                    "1234", Optional.empty());
                    });
    }

    @Test
    void createNewBooking_successSeveralPeople() {

        List<String> otherPersonList = new ArrayList<>();
        otherPersonList.add(otherPerson1.getIdentityCode());
        otherPersonList.add(otherPerson2.getIdentityCode());

        Booking validBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();

        BookingDTO validBookingDTO = BookingMapper.INSTANCE.toDTO(validBooking);

        BDDMockito.given(personRepository.findPersonByIdentityCode("1234")).willReturn(Optional.of(person));
        BDDMockito.given(personRepository.findPersonByIdentityCode("1235")).willReturn(Optional.of(otherPerson1));
        BDDMockito.given(personRepository.findPersonByIdentityCode("1236")).willReturn(Optional.of(otherPerson2));

        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));
        BDDMockito.doNothing().when(personInBookingRepository)
                .addPersonInBooking(validBooking.getId(),
                        validBooking.getPersonId(),
                        validBooking.getPersonIdentityCode());

        BDDMockito.lenient().when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);

        assertDoesNotThrow(() -> {
            bookingService.createNewBooking(
                    validBookingDTO,
                    1L,
                    "1234", Optional.of(otherPersonList));
        });
    }

    @Test
    void updateBooking_success() {

        Booking originalBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 20))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .comments("Something something")
                .lateCheckOut(true)
                .personId(1L)
                .personIdentityCode("1234")
                .build();

        bookingList.add(originalBooking);

        Booking changedBooking = Booking.builder()
                .id(1L)
                .roomId(1L)
                .room(room1)
                .price(35)
                .checkInDate(LocalDate.of(2024, 10, 22))
                .checkOutDate(LocalDate.of(2024, 10, 26))
                .comments("New booking")
                .lateCheckOut(false)
                .personId(1L)
                .personIdentityCode("1234")
                .build();

        BookingDTO changedBookingDTO = BookingMapper.INSTANCE.toDTO(changedBooking);

        // given
        BDDMockito.given(bookingRepository.findAll()).willReturn(bookingList);
        BDDMockito.given(bookingRepository.findById(1L)).willReturn(Optional.of(originalBooking));
        BDDMockito.given(roomRepository.findById(1L)).willReturn(Optional.of(room1));
        BDDMockito.given(personRepository.findPersonByIdentityCode("1234")).willReturn(Optional.of(person));

        // when
        BookingDTO result = bookingService.updateBooking(1L, changedBookingDTO);

        assertEquals(changedBookingDTO.getPrice(), result.getPrice());
        assertEquals(changedBookingDTO.getCheckInDate(), result.getCheckInDate());
        assertEquals(changedBookingDTO.getCheckOutDate(), result.getCheckOutDate());
        assertEquals(changedBookingDTO.getComments(), result.getComments());
        assertEquals(changedBookingDTO.isLateCheckOut(), result.isLateCheckOut());
    }
}