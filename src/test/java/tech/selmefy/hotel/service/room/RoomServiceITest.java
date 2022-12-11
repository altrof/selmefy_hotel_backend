package tech.selmefy.hotel.service.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import tech.selmefy.hotel.AbstractIntegrationTest;
import tech.selmefy.hotel.controller.room.dto.AddNewRoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.service.room.type.RoomType;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RoomServiceITest extends AbstractIntegrationTest {

    @Autowired
    private RoomService roomService;

    @Test
    void deleteRoom_throwsApiRequestException_WhenProvidedDataIsIncorrect() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(100L); // room with id 100 is not exists and should not be created with liquibase script
        roomDTO.setRoomType(RoomType.KING_SIZE.getRoomTypeText());
        roomDTO.setSize(10F);
        roomDTO.setRoomAvailableForBooking(false);
        roomDTO.setRoomNumber(1);
        roomDTO.setNumberOfBeds(5);
        roomDTO.setFloorId(1);

        Room room = RoomMapper.INSTANCE.toEntity(roomDTO);

        assertThrows(ApiRequestException.class, () -> {
            roomService.deleteRoom(room.getId());
        });
    }

    @Test
    void addNewRoom_throwsApiRequestException_WhenProvidedDataIsIncorrect() {
        AddNewRoomDTO roomDTO = new AddNewRoomDTO();
        roomDTO.setRoomType(RoomType.KING_SIZE.getRoomTypeText());
        roomDTO.setSize(10F);
        roomDTO.setRoomAvailableForBooking(false);
        roomDTO.setRoomNumber(100);
        roomDTO.setNumberOfBeds(5);
        roomDTO.setFloorId(1);

        assertThrows(ApiRequestException.class, () -> {
            roomService.addNewRoom(roomDTO);
        });
    }
}
