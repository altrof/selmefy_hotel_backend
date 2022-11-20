package tech.selmefy.hotel.service.booking;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingService {

        public final BookingRepository bookingRepository;
        public final PersonRepository personRepository;
        public final RoomRepository roomRepository;


    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDTO bookingDTO = BookingMapper.INSTANCE.toDTO(booking);
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }

    public void createNewBooking(@NonNull BookingDTO bookingDTO, Long roomId, String personIdentityCode) {
        if (bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new ApiRequestException("Check-out has to be after check-in!");
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ApiRequestException("Check-in cannot be in the past!");
        }

        Room room = roomRepository.findById(roomId).orElseThrow();

        if (!isRoomAvailable(room, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new ApiRequestException("Room is not available at the provided dates!");
        }

        /*
            We find the person based on their identity code as opposed
            to the internal id in the DB. This allows to connect the person
            to a booking without having to check their id from the DB.
        */
        Person person = personRepository.findPersonByIdentityCode(personIdentityCode).orElseThrow();
        Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
        booking.setRoom(room);
        booking.setPerson(person);
        bookingRepository.save(booking);
    }
    private boolean isRoomAvailable(Room room, LocalDate fromDate, LocalDate toDate) {
        /*
        If someone wants to change their check in date then we need to remove their booking
        from the list otherwise we will get "Room is not available" error.
         */
        return isRoomAvailable(room, fromDate, toDate, Optional.empty());
    }
    private boolean isRoomAvailable(Room room, LocalDate fromDate, LocalDate toDate, Optional<Booking> bookingUpdate) {
        if (room.getRoomAvailableForBooking().equals(Boolean.FALSE)) {
            return false;
        } else {
            List<Booking> bookings = bookingRepository.findAll();
            bookingUpdate.ifPresent(bookings::remove);
            for (Booking booking : bookings) {
                if (
                    (Objects.equals(booking.getRoomId(), room.getId()))
                    &&
                        (fromDate.isBefore(booking.getCheckInDate())
                        && toDate.isAfter(booking.getCheckInDate()))
                        ||
                        (fromDate.isBefore(booking.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public BookingDTO getBookingById(Long id) {
        return bookingRepository.findById(id).map(BookingMapper.INSTANCE::toDTO).orElseThrow();
    }

    public BookingDTO updateBooking(Long id, @NonNull BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ApiRequestException("Booking does not exist with id: " + id));

        if (bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new ApiRequestException("Check-out has to be after check-in!");
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ApiRequestException("Check-in cannot be in the past!");
        }

        Room room = roomRepository.findById(bookingDTO.getRoomId()).orElseThrow();
        Person person = personRepository.findPersonByIdentityCode(bookingDTO.getPersonIdentityCode()).orElseThrow();

        if (!isRoomAvailable(room, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate(), Optional.of(booking))) {
            throw new ApiRequestException("Room is not available at the provided dates!");
        }

        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setPrice(bookingDTO.getPrice());
        booking.setComments(bookingDTO.getComments());

        booking.setPerson(person);
        booking.setPersonIdentityCode(bookingDTO.getPersonIdentityCode());

        booking.setRoomId(bookingDTO.getRoomId());
        booking.setRoom(room);

        bookingRepository.save(booking);
        return BookingMapper.INSTANCE.toDTO(booking);
    }
}
