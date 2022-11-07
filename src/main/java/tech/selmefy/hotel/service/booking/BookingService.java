package tech.selmefy.hotel.service.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

        public final BookingRepository bookingRepository;

        public List<BookingDTO> getAllBookings() {
            List<Booking> bookings = bookingRepository.findAll();
            List<BookingDTO> bookingDTOList = new ArrayList<>();
            for (Booking booking : bookings) {
                BookingDTO bookingDTO = BookingMapper.INSTANCE.toDTO(booking);
                bookingDTOList.add(bookingDTO);
            }
            return bookingDTOList;
        }
}
