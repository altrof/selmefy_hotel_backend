package tech.selmefy.hotel.controller.hotelserviceorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelServiceOrderDTO {
    private Short serviceType;
    private String personId;
    private Timestamp orderTime;
    private Long price;
    private String comments;
}
