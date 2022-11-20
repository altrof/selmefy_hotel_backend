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
import tech.selmefy.hotel.repository.personinbooking.PersonInBookingRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import javax.transaction.Transactional;
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

        public final PersonInBookingRepository personInBookingRepository;


    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDTO bookingDTO = BookingMapper.INSTANCE.toDTO(booking);
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }

    /**
     * Creates a new booking based on the BookingDTO and adds all connected people
     * to the person_in_booking table. People that are added need to have been registered
     * in the Person table of the database. If they are not, use PersonService.createNewPerson() first.
     * @param bookingDTO data about the Booking as described in BookingDTO class
     * @param roomId id of the room that is booked.
     * @param ownerIdentityCode personal identity code of the person for whom the room is booked.
     * @param otherGuestsIdentityCodes personal identity codes of all other people
     * that are connected with the booking.
     */
    @Transactional
    public void createNewBooking(@NonNull BookingDTO bookingDTO, Long roomId,
        String ownerIdentityCode, Optional<List<String>> otherGuestsIdentityCodes) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiRequestException("No such room!"));

        validateBookingRequest(bookingDTO, room, Optional.empty());




        Person bookingOwner = personRepository.findPersonByIdentityCode(ownerIdentityCode)
            .orElseThrow(() -> new ApiRequestException("Owner of the booking is not in the database!"));

        Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
        booking.setRoom(room);
        booking.setPerson(bookingOwner);
        bookingRepository.save(booking);
        addPersonInBooking(booking, bookingOwner);

        if (otherGuestsIdentityCodes.isPresent()) {
            List<Person> otherPeople = otherGuestsIdentityCodes.get().stream()
                    .map(idCode -> personRepository.findPersonByIdentityCode(idCode).orElseThrow(
                            () -> new ApiRequestException("Additional guest is not in the database: " + idCode))).toList();
            for (Person person : otherPeople) {
                addPersonInBooking(booking, person);
            }
        }
    }

    /*
    If someone wants to change their check in date then we need to remove their booking
    from the list otherwise we will get "Room is not available" error.
     */

    /**
     * Check whether the data in the bookingDTO is valid
     * and whether the room is available at the period in question.
     * @param bookingDTO Data of the new booking.
     * @param room Room that is being booked.
     * @param originalBooking In case of a POST request, this is empty. In case of PUT
     * @throws ApiRequestException
     */
    private void validateBookingRequest(BookingDTO bookingDTO, Room room, Optional<Booking> originalBooking) throws ApiRequestException {
        if (bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new ApiRequestException("Check-out has to be after check-in!");
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ApiRequestException("Check-in cannot be in the past!");
        }

        if (!isRoomAvailable(room, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate(), originalBooking)) {
            throw new ApiRequestException("Room is not available at the provided dates!");
        }
    }

    private void addPersonInBooking(Booking booking, Person person) {
        personInBookingRepository.addPersonInBooking(booking.getId(),
            person.getId(), person.getIdentityCode());
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

        Room room = roomRepository.findById(bookingDTO.getRoomId())
            .orElseThrow(() -> new ApiRequestException("No such room!"));

        validateBookingRequest(bookingDTO, room, Optional.of(booking));

        Person person = personRepository.findPersonByIdentityCode(bookingDTO.getPersonIdentityCode())
            .orElseThrow(() -> new ApiRequestException("No such person!"));

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
