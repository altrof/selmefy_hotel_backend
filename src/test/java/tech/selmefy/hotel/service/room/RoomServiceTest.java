package tech.selmefy.hotel.service.room;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import tech.selmefy.hotel.controller.room.RoomController;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private List<Room> roomList = new ArrayList<>();
    private Room room1 = new Room(1L, 5F, 1, 1, 5, RoomType.REGULAR, true);
    private Room room2 = new Room(2L, 10F, 1, 2, 2, RoomType.DELUXE, false);
    private Room room3 = new Room(3L, 10F, 1, 2, 2, RoomType.ECONOMY, false);
    private Room room4 = new Room(4L, 10F, 1, 2, 2, RoomType.REGULAR, false);

    private List<Long> roomIdList = Arrays.asList(new Long[]{1L, 2L, 3L, 4L});
    @Mock
    private RoomRepository roomRepository;

    @Spy
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        roomList.clear();
    }

    @Test
    void getAllRooms() {

        // given
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);
        BDDMockito.given(roomRepository.findAll()).willReturn(roomList);

        // when
        List<RoomDTO> result = roomService.getAllRooms();

        // then
        assertEquals(roomList.size(), result.size());

        // Confirming all rooms are here
        for (RoomDTO roomDTO : result) {
            assertTrue(roomIdList.contains(roomDTO.getId()));
        }
    }

    @Test
    void getRoomsByType() {
    }

    @Test
    void getRoomById() {
    }

    @Test
    void getAvailableRooms() {
    }

    @Test
    void getRoomAvailableHistoryByRoomId() {
    }

    @Test
    void updateRoomAvailableForBooking() {
    }
}