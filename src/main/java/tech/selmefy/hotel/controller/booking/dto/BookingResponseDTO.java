package tech.selmefy.hotel.controller.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private List<BookingDTO> bookings;
    private int totalBookingLength;
}
