package tech.selmefy.hotel.service.booking;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    public void createNewBooking(@NonNull BookingDTO bookingDTO, Long roomId, Long personId) {
        // Validation needed.
            Room room = roomRepository.findById(roomId).get();
            Person person = personRepository.findById(personId).get();
            Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
            booking.setRoom(room);
            booking.setPerson(person);
            bookingRepository.save(booking);
        }
}
