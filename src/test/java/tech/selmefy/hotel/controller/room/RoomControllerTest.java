package tech.selmefy.hotel.controller.room;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomResponseDTO;
import tech.selmefy.hotel.service.room.RoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {


    private List<RoomDTO> roomDTOList = new ArrayList<>();
    RoomDTO room1 = new RoomDTO(1L, 5F, 1, 1, 5, "REGULAR", true);
    RoomDTO room2 = new RoomDTO(2L, 10F, 1, 2, 2, "DELUXE", false);
    RoomDTO room3 = new RoomDTO(3L, 10F, 1, 2, 2, "ECONOMY", false);
    RoomDTO room4 = new RoomDTO(4L, 10F, 1, 2, 2, "REGULAR", false);

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;



    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void tearDown() {
        roomDTOList.clear();
    }

    @Test
    void getAllRooms() {

        // given
        roomDTOList.add(room1);
        roomDTOList.add(room2);
        roomDTOList.add(room3);
        roomDTOList.add(room4);
        BDDMockito.given(roomService.getAllRoomsWithParams(0,10,"id", "ASC", Optional.empty(), Optional.empty())).willReturn((RoomResponseDTO) roomDTOList);

        // when
        RoomResponseDTO result = roomController.getAllRooms(0,10, "id", "ASC",Optional.empty(), Optional.empty());

        // then
        assertEquals(roomDTOList.size(), result.getTotalRoomsLength());
        assertTrue(result.getRooms().contains(room1));
        assertTrue(result.getRooms().contains(room2));
    }

    @Test
    void getRoomsByType() {
        roomDTOList.add(room1);
        roomDTOList.add(room4);

        BDDMockito.given(roomService.getRoomsByType("REGULAR")).willReturn(roomDTOList);

        // when
        List<RoomDTO> result = roomController.getRoomsByType("REGULAR");

        // then
        assertEquals(roomDTOList.size(), result.size());
        assertTrue(result.contains(room1));
        assertTrue(result.contains(room4));

        for (RoomDTO roomDTO : result) {
            assertEquals("REGULAR", roomDTO.getRoomType());
        }
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