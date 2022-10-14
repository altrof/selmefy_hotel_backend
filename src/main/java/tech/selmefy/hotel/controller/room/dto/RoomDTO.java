package tech.selmefy.hotel.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;

    private Float size;

    private int floor_id;

    private int room_number;

    private int number_of_beds;
}
