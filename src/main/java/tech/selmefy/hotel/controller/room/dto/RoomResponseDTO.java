package tech.selmefy.hotel.controller.room.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
    private List<RoomDTO> rooms;
    private int totalRoomsLength;
}
