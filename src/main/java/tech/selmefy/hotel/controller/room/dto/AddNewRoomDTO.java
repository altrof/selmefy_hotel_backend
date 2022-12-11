package tech.selmefy.hotel.controller.room.dto;

import lombok.Data;

@Data
public class AddNewRoomDTO {
    private Float size;
    private int floorId;
    private int roomNumber;
    private int numberOfBeds;
    private String roomType;
    private Boolean roomAvailableForBooking;
}
