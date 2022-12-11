package tech.selmefy.hotel.controller.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.selmefy.hotel.AbstractIntegrationTest;
import tech.selmefy.hotel.controller.room.dto.AddNewRoomDTO;
import tech.selmefy.hotel.exception.ApiRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RoomControllerITest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser(roles = "admin")
    @Test
    void deleteRoom_throwsException_WhenProvidedDataIsIncorrect() {
        try {
            mvc.perform(delete("/rooms/{roomId}", 100L));
        } catch (Exception e) {
            assertEquals(ApiRequestException.class, getRootCause(e).getClass());
        }
    }

    @WithMockUser(roles = "admin")
    @Test
    void addNewRoom_returnResponseEntityOk_WhenProvidedDataIsCorrect() throws Exception {
        AddNewRoomDTO requestBody = new AddNewRoomDTO();

        requestBody.setRoomType("REGULAR");
        requestBody.setRoomNumber(1);
        requestBody.setFloorId(1);
        requestBody.setSize(45F);
        requestBody.setNumberOfBeds(2);
        requestBody.setRoomAvailableForBooking(true);


        mvc.perform(post("/rooms").contentType(MediaType.APPLICATION_JSON_VALUE).content(toJsonString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is("New room is successfully created!")));
    }


    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
