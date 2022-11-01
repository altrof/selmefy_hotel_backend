package tech.selmefy.hotel.controller.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long roomId;
    private String personIdentityCode;
    private int price;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String comments;
    private boolean lateCheckOut;
}