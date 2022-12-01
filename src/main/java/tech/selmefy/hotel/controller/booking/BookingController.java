package tech.selmefy.hotel.controller.booking;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.service.booking.BookingService;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @PostMapping(path = "/public", params = {"roomId", "ownerId"})
    public void createNewBooking(@RequestBody BookingDTO bookingDTO,
                                 @RequestParam(name="roomId") Long roomId,
                                 @RequestParam(name="ownerId") String ownerId,
                                 @RequestParam(name="otherId") Optional<List<String>> otherId) {
        bookingService.createNewBooking(bookingDTO, roomId, ownerId, otherId);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long bookingId, @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, bookingDTO));
    }
}