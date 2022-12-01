package tech.selmefy.hotel.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    // For RoomDTO, we actually need to know the id in DB.
    private Long id;
    private Float size;
    private int floorId;
    private int roomNumber;
    private int numberOfBeds;
    private String roomType;
    private Boolean roomAvailableForBooking;
}
