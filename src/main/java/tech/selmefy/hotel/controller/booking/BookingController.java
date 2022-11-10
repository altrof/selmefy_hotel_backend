package tech.selmefy.hotel.controller.booking;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.service.booking.BookingService;

import java.util.List;

@Api(tags = "Booking")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "booking")
public class BookingController {

    public final BookingService bookingService;

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping(params = {"roomId", "personId"})
    public void createNewBooking(@RequestBody BookingDTO bookingDTO,
                                 @RequestParam(name="roomId") Long roomId,
                                 @RequestParam(name = "personId") Long personId) {
        bookingService.createNewBooking(bookingDTO, roomId, personId);
    }
}