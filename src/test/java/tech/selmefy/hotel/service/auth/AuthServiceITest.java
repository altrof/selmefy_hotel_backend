package tech.selmefy.hotel.service.auth;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import tech.selmefy.hotel.AbstractIntegrationTest;
import tech.selmefy.hotel.controller.auth.AuthController;
import tech.selmefy.hotel.controller.auth.dto.request.SignupRequestDTO;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRoleRepository;
import tech.selmefy.hotel.security.jwt.JwtTokenUtil;
import tech.selmefy.hotel.service.person.PersonService;
import tech.selmefy.hotel.service.user_account.UserAccountService;
import tech.selmefy.hotel.utils.AppUtil;
import tech.selmefy.hotel.utils.email_sender.EmailSender;
import tech.selmefy.hotel.utils.email_sender.EmailService;
import tech.selmefy.hotel.validators.EmailValidator;

import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthServiceITest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountRoleRepository userAccountRoleRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService personService;

    @Autowired
    public  PersonRepository personRepository;

    @Autowired
    private EmailConfirmationTokenService emailConfirmationTokenService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private AuthController authController;

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//    }

    @Test
    void register() throws Exception {
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username("testUser")
            .password("testPassword")
            .email("test@email.com")
            .firstName("Testy")
            .lastName("Testiliano")
            .country("Testland")
            .identityCode("12345678")
            .phoneNumber("+37255555555")
            .dateOfBirth(LocalDate.of(1996, 8, 17))
            .build();

        String requestBody = mapper.writeValueAsString(signupRequestDTO);


        mvc.perform(post("/auth/signup")
                .content(requestBody)
                .contentType("application/json")
            )
            .andExpect(status().isOk());
    }
}
