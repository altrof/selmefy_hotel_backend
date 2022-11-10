package tech.selmefy.hotel.controller.hotelserviceorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelServiceOrderDTO {
    private Short serviceType;
    private String personId;
    private Long price;
    private String comments;
}
