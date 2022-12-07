package tech.selmefy.hotel.controller.user_account;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mail.javamail.JavaMailSender;
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
            .andExpect(jsonPath("$[0].personId", is(2)))
            .andExpect(jsonPath("$[0].username", is("marimaja")))
            .andExpect(jsonPath("$[0].password", containsString("$2a$06$")))
            .andExpect(jsonPath("$[0].email", is("test1@email.com")))
            .andExpect(jsonPath("$[0].identityCode", is("49601111234")))
            .andExpect(jsonPath("$[0].roles.[0].id", is(1)))
            .andExpect(jsonPath("$[0].roles.[0].roleType", is("ROLE_DEFAULT_USER")))
            .andExpect(jsonPath("$[0].enabled", is(false)))
            .andExpect(jsonPath("$[0].locked", is(false)))
            .andExpect(jsonPath("$[1].personId", is(3)))
            .andExpect(jsonPath("$[1].username", is("sibul")))
            .andExpect(jsonPath("$[1].password", containsString("$2a$06$")))
            .andExpect(jsonPath("$[1].email", is("test2@email.com")))
            .andExpect(jsonPath("$[1].identityCode", is("37507312222")))
            .andExpect(jsonPath("$[1].roles.[0].id", is(1)))
            .andExpect(jsonPath("$[1].roles.[0].roleType", is("ROLE_DEFAULT_USER")))
            .andExpect(jsonPath("$[1].enabled", is(false)))
            .andExpect(jsonPath("$[1].locked", is(false)));

    }
}
