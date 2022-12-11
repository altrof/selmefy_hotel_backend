package tech.selmefy.hotel.controller.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long roomId;
    /*
    We can also add personId if needed,
    but for now let's only expose personIdentityCode on the DTO
    and see how it goes.
    */
    private String personIdentityCode;
    private int price;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String comments;
    private boolean lateCheckOut;
}