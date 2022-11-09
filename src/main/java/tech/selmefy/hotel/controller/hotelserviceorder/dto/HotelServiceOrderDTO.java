package tech.selmefy.hotel.controller.hotelserviceorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelServiceOrderDTO {
    private short serviceType;
    private String personId;
    private LocalDate orderTime;
    private Long price;
    private String comments;
}
