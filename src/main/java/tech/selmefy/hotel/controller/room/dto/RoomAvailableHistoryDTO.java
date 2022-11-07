package tech.selmefy.hotel.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailableHistoryDTO {

    private Long roomId;

    private Boolean roomAvailableForBooking;

    private Timestamp createdDtime;
}
