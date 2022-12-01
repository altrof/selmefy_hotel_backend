package tech.selmefy.hotel.controller.room;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.service.room.RoomService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @Test
    void getAllRooms() {
        // given
        RoomDTO room1 = new RoomDTO(1L, 5F, 1, 1, 5, "REGULAR", true);
        RoomDTO room2 = new RoomDTO(2L, 10F, 1, 2, 2, "DELUXE", false);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(room1);
        roomDTOList.add(room2);

        BDDMockito.given(roomService.getAllRooms()).willReturn(roomDTOList);

        // when
        List<RoomDTO> result = roomController.getAllRooms();

        // then
        assertEquals(roomDTOList.size(), result.size());
        assertTrue(result.contains(room1));
        assertTrue(result.contains(room2));

    }

    @Test
    void getRoomsByType() {
    }

    @Test
    void getRoomById() {
    }

    @Test
    void getAllAvailableRooms() {
    }

    @Test
    void updateRoomAvailableForBooking() {
    }

    @Test
    void getRoomAvailableHistoryByRoomId() {
    }
}