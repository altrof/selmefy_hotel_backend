package tech.selmefy.hotel.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Float size;
    private int floorId;
    private int roomNumber;
    private int numberOfBeds;

}
