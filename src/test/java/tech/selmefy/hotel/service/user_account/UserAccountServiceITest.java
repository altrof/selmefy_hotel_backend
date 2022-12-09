package tech.selmefy.hotel.service.user_account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.selmefy.hotel.AbstractIntegrationTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserAccountServiceITest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserAccountService userAccountService;

    @WithMockUser(roles = "admin")
    @Test
    void enableUserAccount() throws Exception {

        userAccountService.enableUserAccount("test@selmefy.tech");

        mvc.perform(get("/users"))
            .andExpect(jsonPath("$[0].identityCode", is("111111111")))
            .andExpect(jsonPath("$[0].enabled", is(true)));
    }
}
