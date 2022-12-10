package tech.selmefy.hotel.controller.user_account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.selmefy.hotel.AbstractIntegrationTest;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserAccountControllerITest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles = "admin")
    @Test
    void getAllUsers_returnAllExistUsers() throws Exception {

        mvc.perform(get("/users"))
            .andExpect(jsonPath("$[0].personId", is(10000)))
            .andExpect(jsonPath("$[0].username", is("selmefytest")))
            .andExpect(jsonPath("$[0].password", containsString("$2a$06$")))
            .andExpect(jsonPath("$[0].email", is("test@selmefy.tech")))
            .andExpect(jsonPath("$[0].identityCode", is("111111111")))
            .andExpect(jsonPath("$[0].enabled", is(false)))
            .andExpect(jsonPath("$[0].locked", is(false)));

    }
}
